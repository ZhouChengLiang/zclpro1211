package org.zclpro.service.sign;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.zclpro.common.msg.ResultErrorCode;
import org.zclpro.common.msg.ResultMsgUtil;
import org.zclpro.common.propertiestool.StringManager;
import org.zclpro.db.configmanage.entity.ConfigManage;
import org.zclpro.db.sign.entity.SignPattern;
import org.zclpro.db.sign.entity.SignRecord;
import org.zclpro.db.sign.entity.SignRule;
import org.zclpro.service.common.Constants;
import org.zclpro.service.configmanage.ConfigManageCache;
import org.zclpro.service.configmanage.ConfigManageVO;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignService {
	
	@Autowired
	private ConfigManageCache configManageCache;
	
	@Autowired
	private SignCache signCache;
	
	private static StringManager stringManager = StringManager.getStringManageByFileName(Constants.SIGN_PROPERTIES_PATH);
	
	private static final int PULL_ALMANAC_LIMIT = stringManager.getIntValue("pull.almanac.limit");
	
	private static final String SIGNTIPS = stringManager.getValue("signtips");
	
	private static final String LOCKKEY = "signlock:user:{0}:county:{1}:date:{2}";
	
	/**
	 * 返回有关签到设置的信息
	 * @param countyCode
	 * @return
	 */
	public Map<String,Object> getSignConfigInfo(Integer countyCode){
		Map<String, Object> result = new HashMap<>();
		ConfigManage source = configManageCache.getConfigBackUp(ConfigManage.SIGNIN, countyCode);
		Preconditions.checkArgument(source != null, "当前地市没有初始签到开关配置数据！");
		ConfigManageVO signConfig = ConfigManageVO.convertFrom(source);
		result.put("signConfig", signConfig);
		SignPattern signPattern = signCache.getEfficientSignPattern(countyCode);
		Preconditions.checkArgument(signPattern != null, "当前地市没有初始有效签到方式数据！");
		SignPatternVO spVO = SignPatternVO.convertFrom(signPattern);
		result.put("signPattern", spVO);
		List<SignRule> signRule = getSingRules(signPattern);
		Preconditions.checkArgument(!CollectionUtils.isEmpty(signRule), "当前地市没有初始签到规则数据！");
		result.put("signRule",signRule);
		return result;
	}
	
	private List<SignRule> getSingRules(SignPattern signPattern){
		List<SignRule> result = new ArrayList<>();
		List<Integer> idList = signCache.getSignRuleIdList(signPattern);
		if (CollectionUtils.isEmpty(idList))
            return Collections.EMPTY_LIST;
		for(Integer signRuleId:idList){
			SignRule signRule = signCache.getSignRuleFromCache(signRuleId);
			result.add(signRule);
		}
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void modifySignConfigInfo(SignPattern signPattern,List<SignRule> signRules){
		signCache.modifySignConfigInfo(signPattern,signRules);
	}
	
	/**
	 * 转化一下日期为指定日期,格式为YYYY-MM-DD,如月份和日期小于10,则取个位,如:2012-1-1
	 * @return
	 */
	private String getSpecifyDate(LocalDate localDate){
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return DateFormatUtils.format(date, "yyyy-M-d");
	}
	
	/**
	 * 判断当前用户当天在所属地市下的签到情况
	 * @param userId
	 * @param countyCode
	 * @param curDate
	 * @return true 已签  false 未签 
	 */
	public boolean signed(Integer userId,Integer countyCode,Date curDate){
		String localDateStr = DateFormatUtils.format(curDate, "yyyyMMdd");  
		return signCache.signed(userId, countyCode, localDateStr);
	}
	
	/**
	 * APP端 进入签到页
	 * 检查签到开关
	 * 显示黄历信息(聚合数据接口)
	 * 查询自己的签到状态
	 * @param userId
	 * @param code
	 * @return
	 */
	public Map<String, Object> getSignInfo(Integer userId, Integer countyCode) {
		Map<String,Object> result = new HashMap<>();
		LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
		String localDateStr = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
		ConfigManage configManage = configManageCache.getConfigBackUp(ConfigManage.SIGNIN, countyCode);
		Preconditions.checkArgument(configManage != null, "当前地市没有初始签到开关配置数据！");
		if(Objects.equal(configManage.getConfigStatus(), ConfigManage.CLOSED)){
			return ResultMsgUtil.fail(ResultErrorCode.SIGN_SWITCH_CLOSED);
		}
		boolean signed = signCache.signed(userId, countyCode,localDateStr);
		result.put("signed", signed?1:0);
		SignRecord signRecord = signCache.getSignRecordFromCache(countyCode,userId);
		int serialdays = 0;
		if(signRecord != null){
			serialdays = signRecord.getSerialdays();
		}
		if(signed){
			result.put("serialdays", serialdays);
		}else{
			result.put("signtip", getSignTip());
		}
		String specifyDate = getSpecifyDate(localDate);
		SignCommonInfoVO signCommonInfoVO = signCache.getSignCommonInfo(specifyDate,result);
		result.put("signCommonInfo", signCommonInfoVO);
		SignRule signRule = signCache.getSignRuleByIndex(countyCode, localDate.getDayOfWeek().getValue());
		result.put("signIntegral", signRule.getSignIntegral());
		result.put("signExperience", signRule.getSignExperience());
		return ResultMsgUtil.success(result);
	}
	
	public String getSignTip(){
		List<String> tips = Arrays.asList(SIGNTIPS.split(";"));
		int totalSize = tips.size();
		int index = (int) (System.currentTimeMillis()%totalSize);
		return "“"+ tips.get(index) +"”";
	}
	
	/**
	 * app 端点击签到
	 * 检查签到开关是否开启
	 * 检查自己是否已经签到过
	 * 获取当天的签到奖励
	 * @param userId
	 * @param countyCode
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String,Object> doSignIn(Integer userId, Integer countyCode){
		Map<String,Object> result = new HashMap<>();
		LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
		String currentDate = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
		ConfigManage configManage = configManageCache.getConfigBackUp(ConfigManage.SIGNIN, countyCode);
		Preconditions.checkArgument(configManage != null, "当前地市没有初始签到配置数据！");
		if(Objects.equal(configManage.getConfigStatus(), ConfigManage.CLOSED)){
			return ResultMsgUtil.fail(ResultErrorCode.SIGN_SWITCH_CLOSED);
		}
		String lockKey = StringManager.formatKeyString(LOCKKEY, userId.toString(), countyCode.toString(),currentDate);
		try {
			signCache.lock(lockKey);
			boolean signed = signCache.signed(userId, countyCode,currentDate);
			if(signed){
				return ResultMsgUtil.fail(ResultErrorCode.USER_SIGNED);
			}
			signCache.checkSignRecordFormInsert(countyCode,userId,localDate);
			SignRule signRule = signCache.getSignRuleByIndex(countyCode, localDate.getDayOfWeek().getValue());
			try {
				signCache.operateSignRecord(countyCode, userId, currentDate);
				SignRecord signRecord = signCache.getSignRecordFromCache(countyCode,userId);
				result.put("signExperience", signRule.getSignExperience());
				result.put("signIntegral", signRule.getSignIntegral());
				result.put("serialdays",signRecord.getSerialdays());
			} catch (Exception e) {
				throw e;
			}
		} finally {
			signCache.unlock(lockKey);
		}
		return ResultMsgUtil.success(result);
	}
	
	/**
	 * 提供给定时任务拉取老黄历信息
	 */
	public void pullAlmanacInfo(){
		LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
		int count = 0;
		for(int i = 0;i<10000;i++){
			if (count == PULL_ALMANAC_LIMIT){
				break;
			}
			LocalDate localDateNext = localDate.plusDays(i);
			String specifyDate = getSpecifyDate(localDateNext);
			if(signCache.pullAlmanacInfo(specifyDate, i)){
				try {
					TimeUnit.SECONDS.sleep(new Random().nextInt(10));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
			}
		}
	}
}
