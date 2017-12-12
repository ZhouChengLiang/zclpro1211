package org.zclpro.service.constellatory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.zclpro.common.httpclient.HttpClientUtil;
import org.zclpro.common.propertiestool.StringManager;
import org.zclpro.db.constellatory.entity.ConstellatoryDay;
import org.zclpro.db.constellatory.entity.ConstellatoryMonth;
import org.zclpro.db.constellatory.entity.ConstellatoryWeek;
import org.zclpro.db.constellatory.entity.ConstellatoryYear;
import org.zclpro.db.constellatory.impl.ConstellatoryDayMapper;
import org.zclpro.db.constellatory.impl.ConstellatoryMonthMapper;
import org.zclpro.db.constellatory.impl.ConstellatoryWeekMapper;
import org.zclpro.db.constellatory.impl.ConstellatoryYearMapper;
import org.zclpro.service.common.BaseCache;
import org.zclpro.service.common.Constants;
import org.zclpro.service.enums.ConstellatoryEnum;
import org.zclpro.service.enums.FortuneConditionEnum;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.mysql.jdbc.StringUtils;

@Service
public class ConstellatoryCache extends BaseCache {
	private final static StringManager stringManager = StringManager
			.getStringManageByFileName(Constants.CONSTELLATORY_PROPERTIES_PATH);

	private final static String APPKEY = stringManager.getValue("appkey");

	private final static String URL = stringManager.getValue("url");

	private DayHandler dayHandler = new DayHandler();

	private WeekHandler weekHandler = new WeekHandler();

	private MonthHandler monthHandler = new MonthHandler();

	private YearHandler yearHandler = new YearHandler();

	@Autowired
	private ConstellatoryDayMapper constellatoryDayMapper;

	@Autowired
	private ConstellatoryWeekMapper constellatoryWeekMapper;

	@Autowired
	private ConstellatoryMonthMapper constellatoryMonthMapper;

	@Autowired
	private ConstellatoryYearMapper constellatoryYearMapper;

	/**
	 * 希望这个可以达到理想的效果,单元测试中不加睡眠时间竟然没有用
	 */
	public void pullConstellatory3() {
		ExecutorService executor = Executors.newCachedThreadPool();
		// 处理 today,tomorrow
		executor.execute(() -> pullRemoteConstellatory(dayHandler));
		// 处理week,nextweek
		executor.execute(() -> pullRemoteConstellatory(weekHandler));
		// 处理month
		executor.execute(() -> pullRemoteConstellatory(monthHandler));
		// 处理year
		executor.execute(() -> pullRemoteConstellatory(yearHandler));

		executor.shutdown();
	}

	private void pullRemoteConstellatory(RemoteConstellatoryHandler rclher) {
		rclher.process();
	}

	/**
	 * 处理十二星座 关于 today,tomorrow的信息
	 * 
	 * @author Administrator
	 *
	 */
	private class DayHandler extends RemoteConstellatoryHandler {

		@Override
		protected void middleProcess(FortuneConditionEnum fortuneConditionEnum, ConstellatoryEnum constellatoryEnum,
				List list) {
			Map<String, Object> params = new HashMap<>();
			boolean needSaveToDB = true;
			LocalDate curdate = LocalDate.now();
			String dayWithMonthYear = curdate.toString("yyyyMMdd");
			if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TOMOROW)) {
				dayWithMonthYear = curdate.plusDays(1).toString("yyyyMMdd");
			}
			byte[] cacheKey = StringManager
					.formatKeyString("consCode:{0}:day:{1} ", constellatoryEnum.getCode().toString(), dayWithMonthYear)
					.getBytes(Charsets.UTF_8);
			byte[] redisBytes = redisCache.get(cacheKey);
			if (redisBytes != null) {
				needSaveToDB = false;
			}
			if (needSaveToDB) {
				params.put("key", APPKEY);
				params.put("consName", constellatoryEnum.getName());
				params.put("type", fortuneConditionEnum.getName());
				String result = HttpClientUtil.postRequest(URL, params);
				if (!StringUtils.isNullOrEmpty(result)) {
					JSONObject object = JSONObject.parseObject(result);
					ConstellatoryDay constellatoryDay = JSONObject.parseObject(result, ConstellatoryDay.class);
					constellatoryDay.setCode(constellatoryEnum.getCode());
					constellatoryDay.setDatetimeStr(object.getString("datetime"));
					constellatoryDay.setFriend(object.getString("QFriend"));
					String dateId = curdate.toString("yyyyMMdd");
					if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TOMOROW)) {
						dateId = curdate.plusDays(1).toString("yyyyMMdd");
					}
					constellatoryDay.setDateId(dateId);
					constellatoryDay.setAlls(object.getString("all"));
					list.add(constellatoryDay);
					redisCache.set(cacheKey, SerializationUtils.serialize(constellatoryDay));
				}
			}
		}

		@Override
		protected List<FortuneConditionEnum> getFortuneConditionEnums() {
			return Arrays.asList(FortuneConditionEnum.TODAY, FortuneConditionEnum.TOMOROW);
		}

		@Override
		protected void finalProcess(List list) {
			if (!CollectionUtils.isEmpty(list)) {
				constellatoryDayMapper.batchSaveOrUpdate(list);
			}
		}

	}

	/**
	 * 处理十二星座 关于 week,nextweek的信息
	 * 
	 * @author Administrator
	 *
	 */
	private class WeekHandler extends RemoteConstellatoryHandler {

		@Override
		protected void middleProcess(FortuneConditionEnum fortuneConditionEnum, ConstellatoryEnum constellatoryEnum,
				List list) {
			Map<String, Object> params = new HashMap<>();
			boolean needSaveToDB = true;
			LocalDate curdate = LocalDate.now();
			String year = String.valueOf(curdate.getYear());
			String week = String.valueOf(curdate.getWeekOfWeekyear());
			if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.NEXTWEEK)) {
				week = String.valueOf(curdate.withDayOfWeek(1).plusWeeks(1).getWeekOfWeekyear());
				year = String.valueOf(curdate.withDayOfWeek(1).plusWeeks(1).getWeekyear());
			}
			byte[] cacheKey = StringManager.formatKeyString("consCode:{0}:year:{1}:week:{2}",
					constellatoryEnum.getCode().toString(), year, week).getBytes(Charsets.UTF_8);
			byte[] redisBytes = redisCache.get(cacheKey);
			if (redisBytes != null) {
				needSaveToDB = false;
			}
			if (needSaveToDB) {
				params.put("key", APPKEY);
				params.put("consName", constellatoryEnum.getName());
				params.put("type", fortuneConditionEnum.getName());
				String result = HttpClientUtil.postRequest(URL, params);
				if (!StringUtils.isNullOrEmpty(result)) {
					ConstellatoryWeek constellatoryWeek = JSONObject.parseObject(result, ConstellatoryWeek.class);
					constellatoryWeek.setCode(constellatoryEnum.getCode());
					constellatoryWeek.setYear(year);
					list.add(constellatoryWeek);
					redisCache.set(cacheKey, SerializationUtils.serialize(constellatoryWeek));
				}
			}

		}

		@Override
		protected List<FortuneConditionEnum> getFortuneConditionEnums() {
			return Arrays.asList(FortuneConditionEnum.WEEK, FortuneConditionEnum.NEXTWEEK);
		}

		@Override
		protected void finalProcess(List list) {
			if (!CollectionUtils.isEmpty(list)) {
				constellatoryWeekMapper.batchSaveOrUpdate(list);
			}
		}
	}

	/**
	 * 处理十二星座 关于 month的信息
	 * 
	 * @author Administrator
	 *
	 */
	private class MonthHandler extends RemoteConstellatoryHandler {

		@Override
		protected void middleProcess(FortuneConditionEnum fortuneConditionEnum, ConstellatoryEnum constellatoryEnum,
				List list) {
			Map<String, Object> params = new HashMap<>();
			boolean needSaveToDB = true;
			LocalDate curdate = LocalDate.now();
			String year = String.valueOf(curdate.getYear());
			String month = String.valueOf(curdate.getMonthOfYear());
			byte[] cacheKey = StringManager.formatKeyString("consCode:{0}:year:{1}:month:{2}",
					constellatoryEnum.getCode().toString(), year, month).getBytes(Charsets.UTF_8);
			byte[] redisBytes = redisCache.get(cacheKey);
			if (redisBytes != null) {
				needSaveToDB = false;
			}
			if (needSaveToDB) {
				params.put("key", APPKEY);
				params.put("consName", constellatoryEnum.getName());
				params.put("type", fortuneConditionEnum.getName());
				String result = HttpClientUtil.postRequest(URL, params);
				if (!StringUtils.isNullOrEmpty(result)) {
					JSONObject object = JSONObject.parseObject(result);
					ConstellatoryMonth constellatoryMonth = JSONObject.parseObject(result, ConstellatoryMonth.class);
					constellatoryMonth.setCode(constellatoryEnum.getCode());
					constellatoryMonth.setYear(year);
					constellatoryMonth.setAlls(object.getString("all"));
					list.add(constellatoryMonth);
					redisCache.set(cacheKey, SerializationUtils.serialize(constellatoryMonth));
				}
			}
		}

		@Override
		protected List<FortuneConditionEnum> getFortuneConditionEnums() {
			return Arrays.asList(FortuneConditionEnum.MONTH);
		}

		@Override
		protected void finalProcess(List list) {
			if (!CollectionUtils.isEmpty(list)) {
				constellatoryMonthMapper.batchSaveOrUpdate(list);
			}
		}

	}

	/**
	 * 处理十二星座 关于 year的信息
	 * 
	 * @author Administrator
	 *
	 */
	private class YearHandler extends RemoteConstellatoryHandler {

		@Override
		protected void middleProcess(FortuneConditionEnum fortuneConditionEnum, ConstellatoryEnum constellatoryEnum,
				List list) {
			Map<String, Object> params = new HashMap<>();
			boolean needSaveToDB = true;
			LocalDate curdate = LocalDate.now();
			String year = String.valueOf(curdate.getYear());
			byte[] cacheKey = StringManager
					.formatKeyString("consCode:{0}:year:{1}", constellatoryEnum.getCode().toString(), year)
					.getBytes(Charsets.UTF_8);
			byte[] redisBytes = redisCache.get(cacheKey);
			if (redisBytes != null) {
				needSaveToDB = false;
			}
			if (needSaveToDB) {
				params.put("key", APPKEY);
				params.put("consName", constellatoryEnum.getName());
				params.put("type", fortuneConditionEnum.getName());
				String result = HttpClientUtil.postRequest(URL, params);
				if (!StringUtils.isNullOrEmpty(result)) {
					JSONObject object = JSONObject.parseObject(result);
					ConstellatoryYear constellatoryYear = JSONObject.parseObject(result, ConstellatoryYear.class);
					constellatoryYear.setCode(constellatoryEnum.getCode());
					constellatoryYear.setShortInfo(object.getJSONObject("mima").get("info").toString());
					constellatoryYear.setDetailInfo(object.getJSONObject("mima").getJSONArray("text").getString(0));
					constellatoryYear.setCareer(object.getJSONArray("career").getString(0));
					constellatoryYear.setLove(object.getJSONArray("love").getString(0));
					constellatoryYear.setHealth(object.getJSONArray("health").getString(0));
					constellatoryYear.setFinance(object.getJSONArray("finance").getString(0));
					list.add(constellatoryYear);
					redisCache.set(cacheKey, SerializationUtils.serialize(constellatoryYear));
				}
			}
		}

		@Override
		protected List<FortuneConditionEnum> getFortuneConditionEnums() {
			return Arrays.asList(FortuneConditionEnum.YEAR);
		}

		@Override
		protected void finalProcess(List list) {
			if (!CollectionUtils.isEmpty(list)) {
				constellatoryYearMapper.batchSaveOrUpdate(list);
			}
		}

	}

	abstract class RemoteConstellatoryHandler {
		public void process() {
			List list = new ArrayList<>();
			for (ConstellatoryEnum constellatoryEnum : ConstellatoryEnum.values()) {
				for (FortuneConditionEnum fortuneConditionEnum : getFortuneConditionEnums()) {
					middleProcess(fortuneConditionEnum, constellatoryEnum, list);
				}
			}
			finalProcess(list);
		}

		protected abstract void middleProcess(FortuneConditionEnum fortuneConditionEnum,
				ConstellatoryEnum constellatoryEnum, List list);

		protected abstract void finalProcess(List list);

		protected abstract List<FortuneConditionEnum> getFortuneConditionEnums();

	}

	@Override
	public int getExpire() {
		return 0;
	}

}
