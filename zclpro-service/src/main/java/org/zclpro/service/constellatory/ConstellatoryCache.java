package org.zclpro.service.constellatory;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.time.DateFormatUtils;
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
import com.google.common.base.Objects;

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

	public void pullConstellatory() {
		for (ConstellatoryEnum constellatoryEnum : ConstellatoryEnum.values()) {
			for (FortuneConditionEnum fortuneConditionEnum : FortuneConditionEnum.values()) {
				insertConstellatory(constellatoryEnum, fortuneConditionEnum);
			}
		}

	}

	public void insertConstellatory(ConstellatoryEnum constellatoryEnum, FortuneConditionEnum fortuneConditionEnum) {
		Map<String, Object> params = new HashMap<>();
		params.put("key", APPKEY);
		params.put("consName", constellatoryEnum.getName());
		params.put("type", fortuneConditionEnum.getName());
		String result = HttpClientUtil.postRequest(URL, params);
		JSONObject object = JSONObject.parseObject(result);
		if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TODAY)
				|| Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TOMOROW)) {
			LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
			Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			String dateId = DateFormatUtils.format(date, "yyyyMMdd");
			ConstellatoryDay constellatoryDay = JSONObject.parseObject(result, ConstellatoryDay.class);
			constellatoryDay.setCode(constellatoryEnum.getCode());
			constellatoryDay.setDatetimeStr(object.getString("datetime"));
			constellatoryDay.setFriend(object.getString("QFriend"));
			if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TOMOROW)) {
				date = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
				dateId = DateFormatUtils.format(date, "yyyyMMdd");
			}
			constellatoryDay.setDateId(dateId);
			constellatoryDay.setAlls(object.getString("all"));
			constellatoryDayMapper.saveOrUpdate(constellatoryDay);
		} else if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.WEEK)
				|| Objects.equal(fortuneConditionEnum, FortuneConditionEnum.NEXTWEEK)) {
			LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
			ConstellatoryWeek constellatoryWeek = JSONObject.parseObject(result, ConstellatoryWeek.class);
			constellatoryWeek.setCode(constellatoryEnum.getCode());
			constellatoryWeek.setYear(String.valueOf(localDate.getYear()));
			constellatoryWeekMapper.saveOrUpdate(constellatoryWeek);
		} else if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.MONTH)) {
			LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
			ConstellatoryMonth constellatoryMonth = JSONObject.parseObject(result, ConstellatoryMonth.class);
			constellatoryMonth.setCode(constellatoryEnum.getCode());
			constellatoryMonth.setYear(String.valueOf(localDate.getYear()));
			constellatoryMonth.setAlls(object.getString("all"));
			constellatoryMonthMapper.saveOrUpdate(constellatoryMonth);
		} else if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.YEAR)) {
			ConstellatoryYear constellatoryYear = JSONObject.parseObject(result, ConstellatoryYear.class);
			constellatoryYear.setCode(constellatoryEnum.getCode());
			constellatoryYear.setShortInfo(object.getJSONObject("mima").get("info").toString());
			constellatoryYear.setDetailInfo(object.getJSONObject("mima").getJSONArray("text").getString(0));
			constellatoryYear.setCareer(object.getJSONArray("career").getString(0));
			constellatoryYear.setLove(object.getJSONArray("love").getString(0));
			constellatoryYear.setHealth(object.getJSONArray("health").getString(0));
			constellatoryYear.setFinance(object.getJSONArray("finance").getString(0));
			constellatoryYearMapper.saveOrUpdate(constellatoryYear);
		}
	}

	public void pullConstellatory1() {
		Map<String, Object> params = new HashMap<>();
		params.put("key", APPKEY);
		List<ConstellatoryDay> constellatoryDayList = new ArrayList<>();
		List<ConstellatoryWeek> constellatoryWeekList = new ArrayList<>();
		List<ConstellatoryMonth> constellatoryMonthList = new ArrayList<>();
		List<ConstellatoryYear> constellatoryYearList = new ArrayList<>();
		for (ConstellatoryEnum constellatoryEnum : ConstellatoryEnum.values()) {
			for (FortuneConditionEnum fortuneConditionEnum : FortuneConditionEnum.values()) {
				params.put("consName", constellatoryEnum.getName());
				params.put("type", fortuneConditionEnum.getName());
				String result = HttpClientUtil.postRequest(URL, params);
				JSONObject object = JSONObject.parseObject(result);
				if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TODAY)
						|| Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TOMOROW)) {
					LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
					Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
					String dateId = DateFormatUtils.format(date, "yyyyMMdd");
					ConstellatoryDay constellatoryDay = JSONObject.parseObject(result, ConstellatoryDay.class);
					constellatoryDay.setCode(constellatoryEnum.getCode());
					constellatoryDay.setDatetimeStr(object.getString("datetime"));
					constellatoryDay.setFriend(object.getString("QFriend"));
					if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TOMOROW)) {
						date = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
						dateId = DateFormatUtils.format(date, "yyyyMMdd");
					}
					constellatoryDay.setDateId(dateId);
					constellatoryDay.setAlls(object.getString("all"));
					constellatoryDayList.add(constellatoryDay);
				} else if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.WEEK)
						|| Objects.equal(fortuneConditionEnum, FortuneConditionEnum.NEXTWEEK)) {
					LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
					ConstellatoryWeek constellatoryWeek = JSONObject.parseObject(result, ConstellatoryWeek.class);
					constellatoryWeek.setCode(constellatoryEnum.getCode());
					constellatoryWeek.setYear(String.valueOf(localDate.getYear()));
					constellatoryWeekList.add(constellatoryWeek);
				} else if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.MONTH)) {
					LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
					ConstellatoryMonth constellatoryMonth = JSONObject.parseObject(result, ConstellatoryMonth.class);
					constellatoryMonth.setCode(constellatoryEnum.getCode());
					constellatoryMonth.setYear(String.valueOf(localDate.getYear()));
					constellatoryMonth.setAlls(object.getString("all"));
					constellatoryMonthList.add(constellatoryMonth);
				} else if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.YEAR)) {
					ConstellatoryYear constellatoryYear = JSONObject.parseObject(result, ConstellatoryYear.class);
					constellatoryYear.setCode(constellatoryEnum.getCode());
					constellatoryYear.setShortInfo(object.getJSONObject("mima").get("info").toString());
					constellatoryYear.setDetailInfo(object.getJSONObject("mima").getJSONArray("text").getString(0));
					constellatoryYear.setCareer(object.getJSONArray("career").getString(0));
					constellatoryYear.setLove(object.getJSONArray("love").getString(0));
					constellatoryYear.setHealth(object.getJSONArray("health").getString(0));
					constellatoryYear.setFinance(object.getJSONArray("finance").getString(0));
					constellatoryYearList.add(constellatoryYear);
				}
			}
		}
		if (!CollectionUtils.isEmpty(constellatoryDayList)) {
			constellatoryDayMapper.batchSaveOrUpdate(constellatoryDayList);
		}
		if (!CollectionUtils.isEmpty(constellatoryWeekList)) {
			constellatoryWeekMapper.batchSaveOrUpdate(constellatoryWeekList);
		}
		if (!CollectionUtils.isEmpty(constellatoryMonthList)) {
			constellatoryMonthMapper.batchSaveOrUpdate(constellatoryMonthList);
		}
		if (!CollectionUtils.isEmpty(constellatoryYearList)) {
			constellatoryYearMapper.batchSaveOrUpdate(constellatoryYearList);
		}
	}

	/**
	 * 考虑采用多线程拉取数据 预计开启四个线程计算出所需的批处理集合 这个写的有问题
	 */
	public void pullConstellatory2() {
		ExecutorService executor = Executors.newCachedThreadPool();
		for (FortuneConditionEnum fortuneConditionEnum : FortuneConditionEnum.values()) {
			executor.execute(new HandleConstellatory(fortuneConditionEnum));
			// executor.submit(new HandleConstellatory(fortuneConditionEnum));
			/*try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		executor.shutdown();
	}

	public class HandleConstellatory implements Runnable {

		private FortuneConditionEnum fortuneConditionEnum;

		public HandleConstellatory(FortuneConditionEnum fortuneConditionEnum) {
			this.fortuneConditionEnum = fortuneConditionEnum;
		}

		@Override
		public void run() {
			List<ConstellatoryDay> constellatoryDayList = new ArrayList<>();
			List<ConstellatoryWeek> constellatoryWeekList = new ArrayList<>();
			List<ConstellatoryMonth> constellatoryMonthList = new ArrayList<>();
			List<ConstellatoryYear> constellatoryYearList = new ArrayList<>();
			Map<String, Object> params = new HashMap<>();
			params.put("key", APPKEY);
			for (ConstellatoryEnum constellatoryEnum : ConstellatoryEnum.values()) {
				params.put("consName", constellatoryEnum.getName());
				params.put("type", fortuneConditionEnum.getName());
				String result = HttpClientUtil.postRequest(URL, params);
				JSONObject object = JSONObject.parseObject(result);
				if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TODAY)
						|| Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TOMOROW)) {
					LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
					Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
					String dateId = DateFormatUtils.format(date, "yyyyMMdd");
					ConstellatoryDay constellatoryDay = JSONObject.parseObject(result, ConstellatoryDay.class);
					constellatoryDay.setCode(constellatoryEnum.getCode());
					constellatoryDay.setDatetimeStr(object.getString("datetime"));
					constellatoryDay.setFriend(object.getString("QFriend"));
					if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TOMOROW)) {
						date = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
						dateId = DateFormatUtils.format(date, "yyyyMMdd");
					}
					constellatoryDay.setDateId(dateId);
					constellatoryDay.setAlls(object.getString("all"));
					constellatoryDayList.add(constellatoryDay);
				} else if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.WEEK)
						|| Objects.equal(fortuneConditionEnum, FortuneConditionEnum.NEXTWEEK)) {
					LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
					ConstellatoryWeek constellatoryWeek = JSONObject.parseObject(result, ConstellatoryWeek.class);
					constellatoryWeek.setCode(constellatoryEnum.getCode());
					constellatoryWeek.setYear(String.valueOf(localDate.getYear()));
					constellatoryWeekList.add(constellatoryWeek);
				} else if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.MONTH)) {
					LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
					ConstellatoryMonth constellatoryMonth = JSONObject.parseObject(result, ConstellatoryMonth.class);
					constellatoryMonth.setCode(constellatoryEnum.getCode());
					constellatoryMonth.setYear(String.valueOf(localDate.getYear()));
					constellatoryMonth.setAlls(object.getString("all"));
					constellatoryMonthList.add(constellatoryMonth);
				} else if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.YEAR)) {
					ConstellatoryYear constellatoryYear = JSONObject.parseObject(result, ConstellatoryYear.class);
					constellatoryYear.setCode(constellatoryEnum.getCode());
					constellatoryYear.setShortInfo(object.getJSONObject("mima").get("info").toString());
					constellatoryYear.setDetailInfo(object.getJSONObject("mima").getJSONArray("text").getString(0));
					constellatoryYear.setCareer(object.getJSONArray("career").getString(0));
					constellatoryYear.setLove(object.getJSONArray("love").getString(0));
					constellatoryYear.setHealth(object.getJSONArray("health").getString(0));
					constellatoryYear.setFinance(object.getJSONArray("finance").getString(0));
					constellatoryYearList.add(constellatoryYear);
				}
			}
			if (!CollectionUtils.isEmpty(constellatoryDayList)) {
				Instant c1 = Instant.now();
				constellatoryDayMapper.batchSaveOrUpdate(constellatoryDayList);
				System.out.println("constellatoryDayMapper.batchSaveOrUpdate>>>>>>" + Duration.between(c1, Instant.now()));
			}
			if (!CollectionUtils.isEmpty(constellatoryWeekList)) {
				Instant c1 = Instant.now();
				constellatoryWeekMapper.batchSaveOrUpdate(constellatoryWeekList);
				System.out.println("constellatoryWeekMapper.batchSaveOrUpdate>>>>>>" + Duration.between(c1, Instant.now()));
			}
			if (!CollectionUtils.isEmpty(constellatoryMonthList)) {
				Instant c1 = Instant.now();
				constellatoryMonthMapper.batchSaveOrUpdate(constellatoryMonthList);
				System.out.println("constellatoryMonthMapper.batchSaveOrUpdate>>>>>>" + Duration.between(c1, Instant.now()));
			}
			if (!CollectionUtils.isEmpty(constellatoryYearList)) {
				System.out.println(constellatoryYearList.isEmpty());
				Instant c1 = Instant.now();
				constellatoryYearMapper.batchSaveOrUpdate(constellatoryYearList);
				System.out.println("constellatoryYearMapper.batchSaveOrUpdate>>>>>>" + Duration.between(c1, Instant.now()));
			}
		}

	}

	/**
	 * 希望这个可以达到理想的效果,单元测试中不加睡眠时间竟然没有用
	 */
	public void pullConstellatory3() {
		ExecutorService executor = Executors.newCachedThreadPool();
		// 处理 today,tomorrow
		executor.execute(() -> pullRemoteConstellatory(dayHandler));
		/*try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		// 处理week,nextweek
		executor.execute(() -> pullRemoteConstellatory(weekHandler));
		/*try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		// 处理month
		executor.execute(() -> pullRemoteConstellatory(monthHandler));
		/*try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		// 处理year
		executor.execute(() -> pullRemoteConstellatory(yearHandler));
		/*try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

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
		protected void middleProcess(String result, JSONObject object, FortuneConditionEnum fortuneConditionEnum,
				ConstellatoryEnum constellatoryEnum, List list) {
			if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TODAY)
					|| Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TOMOROW)) {
				LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
				Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				String dateId = DateFormatUtils.format(date, "yyyyMMdd");
				ConstellatoryDay constellatoryDay = JSONObject.parseObject(result, ConstellatoryDay.class);
				constellatoryDay.setCode(constellatoryEnum.getCode());
				constellatoryDay.setDatetimeStr(object.getString("datetime"));
				constellatoryDay.setFriend(object.getString("QFriend"));
				if (Objects.equal(fortuneConditionEnum, FortuneConditionEnum.TOMOROW)) {
					date = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
					dateId = DateFormatUtils.format(date, "yyyyMMdd");
				}
				constellatoryDay.setDateId(dateId);
				constellatoryDay.setAlls(object.getString("all"));
				list.add(constellatoryDay);
			}

		}

		@Override
		protected List<FortuneConditionEnum> getFortuneConditionEnums() {
			return Arrays.asList(FortuneConditionEnum.TODAY, FortuneConditionEnum.TOMOROW);
		}

		@Override
		protected List getResultList() {
			return new ArrayList<>();
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
		protected void middleProcess(String result, JSONObject object, FortuneConditionEnum fortuneConditionEnum,
				ConstellatoryEnum constellatoryEnum, List list) {
			LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
			ConstellatoryWeek constellatoryWeek = JSONObject.parseObject(result, ConstellatoryWeek.class);
			constellatoryWeek.setCode(constellatoryEnum.getCode());
			constellatoryWeek.setYear(String.valueOf(localDate.getYear()));
			list.add(constellatoryWeek);
		}

		@Override
		protected List<FortuneConditionEnum> getFortuneConditionEnums() {
			return Arrays.asList(FortuneConditionEnum.WEEK, FortuneConditionEnum.NEXTWEEK);
		}

		@Override
		protected List getResultList() {
			return new ArrayList<>();
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
		protected void middleProcess(String result, JSONObject object, FortuneConditionEnum fortuneConditionEnum,
				ConstellatoryEnum constellatoryEnum, List list) {
			LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
			ConstellatoryMonth constellatoryMonth = JSONObject.parseObject(result, ConstellatoryMonth.class);
			constellatoryMonth.setCode(constellatoryEnum.getCode());
			constellatoryMonth.setYear(String.valueOf(localDate.getYear()));
			constellatoryMonth.setAlls(object.getString("all"));
			list.add(constellatoryMonth);
		}

		@Override
		protected List<FortuneConditionEnum> getFortuneConditionEnums() {
			return Arrays.asList(FortuneConditionEnum.MONTH);
		}

		@Override
		protected List getResultList() {
			return new ArrayList<>();
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
		protected void middleProcess(String result, JSONObject object, FortuneConditionEnum fortuneConditionEnum,
				ConstellatoryEnum constellatoryEnum, List list) {
			ConstellatoryYear constellatoryYear = JSONObject.parseObject(result, ConstellatoryYear.class);
			constellatoryYear.setCode(constellatoryEnum.getCode());
			constellatoryYear.setShortInfo(object.getJSONObject("mima").get("info").toString());
			constellatoryYear.setDetailInfo(object.getJSONObject("mima").getJSONArray("text").getString(0));
			constellatoryYear.setCareer(object.getJSONArray("career").getString(0));
			constellatoryYear.setLove(object.getJSONArray("love").getString(0));
			constellatoryYear.setHealth(object.getJSONArray("health").getString(0));
			constellatoryYear.setFinance(object.getJSONArray("finance").getString(0));
			list.add(constellatoryYear);
		}

		@Override
		protected List<FortuneConditionEnum> getFortuneConditionEnums() {
			return Arrays.asList(FortuneConditionEnum.YEAR);
		}

		@Override
		protected List getResultList() {
			return new ArrayList<>();
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
			Map<String, Object> params = new HashMap<>();
			params.put("key", APPKEY);
			List list = getResultList();
			for (ConstellatoryEnum constellatoryEnum : ConstellatoryEnum.values()) {
				for (FortuneConditionEnum fortuneConditionEnum : getFortuneConditionEnums()) {
					params.put("consName", constellatoryEnum.getName());
					params.put("type", fortuneConditionEnum.getName());
					String result = HttpClientUtil.postRequest(URL, params);
					JSONObject object = JSONObject.parseObject(result);
					middleProcess(result, object, fortuneConditionEnum, constellatoryEnum, list);
				}
			}
			finalProcess(list);
		}

		protected abstract void middleProcess(String result, JSONObject object,
				FortuneConditionEnum fortuneConditionEnum, ConstellatoryEnum constellatoryEnum, List list);

		protected abstract void finalProcess(List list);

		protected abstract List<FortuneConditionEnum> getFortuneConditionEnums();

		protected abstract List getResultList();
	}

	@Override
	public int getExpire() {
		return 0;
	}

}
