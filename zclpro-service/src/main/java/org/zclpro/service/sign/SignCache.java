package org.zclpro.service.sign;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.zclpro.common.httpclient.HttpClientUtil;
import org.zclpro.common.propertiestool.StringManager;
import org.zclpro.db.almanacinfo.entity.AlmanacInfo;
import org.zclpro.db.almanacinfo.impl.AlmanacInfoMapper;
import org.zclpro.db.sign.entity.SignPattern;
import org.zclpro.db.sign.entity.SignRecord;
import org.zclpro.db.sign.entity.SignRule;
import org.zclpro.db.sign.impl.SignPatternMapper;
import org.zclpro.db.sign.impl.SignRecordMapper;
import org.zclpro.db.sign.impl.SignRuleMapper;
import org.zclpro.service.common.BaseCache;
import org.zclpro.service.common.Constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignCache extends BaseCache{
	
	@Autowired
	private SignPatternMapper signPatternMapper;
	
	@Autowired
	private SignRuleMapper signRuleMapper;
	
	@Autowired
	private SignRecordMapper signRecordMapper;
	
	@Autowired
	private AlmanacInfoMapper almanacInfoMapper;
	
	private final static StringManager stringManager = StringManager.getStringManageByFileName(Constants.SIGN_PROPERTIES_PATH);
	
	/**
	 * 标识签到方式种类的签到模式
	 * signType
	 * 0  按周发送奖励
	 * 1 自定义
	 */
	private final static String SIGNPATTERN_SIGNTYPE = stringManager.getValue("signpattern.signtype");
	
	/**
	 * 标识当前 生效的签到模式
	 * signStaus 为1 则该条签到模式为当前所使用
	 */
	private final static String SIGNPATTERN_EFFECTIVE = stringManager.getValue("signpattern.effective");
	
	private final static String SIGNRULE = stringManager.getValue("signrule");
	
	private final static String SIGNEDORNOT = stringManager.getValue("signedornot");
	
	private int expire = stringManager.getIntValue("sign_expire");
	
	private final static String APPKEY = stringManager.getValue("appkey");
	
	private final static String URL = stringManager.getValue("url");;
	
	private final static String SIGNCOMMONINFO = stringManager.getValue("signcommoninfo");
	
	private final static String SIGNRECORD = stringManager.getValue("signrecord");
	
	@Override
	public int getExpire() {
		return expire;
	}
	
	
	/**
	 * 供加载生效的
	 * 签到模式使用 进入签到页面 加载生效的签到方式与其对应下的签到规则
	 * @param countyCode
	 * @param signStatus
	 * @return
	 */
	public SignPattern getEfficientSignPattern(Integer countyCode){
		String key = StringManager.formatKeyString(SIGNPATTERN_EFFECTIVE, countyCode.toString());
		if(needFillCache(key)){
			SignPattern signPattern = new SignPattern();
			signPattern.setCountyCode(countyCode);
			signPattern.setSignStatus(SignPattern.EFFECTIVE);
			SignPattern sp = signPatternMapper.selectByCondition(signPattern);
			if(sp == null){
				return null;
			}
			redisCache.hmset(sp.getId().toString(), sp);
			redisCache.set(key, sp.getId().toString());
		}
		Preconditions.checkArgument(StringUtils.isNotEmpty(redisCache.get(key)), "当前地市没有初始签到方式信息数据！");
		return getSignPatternFromCache(Integer.valueOf(redisCache.get(key)));
	}
	
	/**
	 * 获取签到规则
	 * @param countyCode
	 * @return
	 */
	public List<Integer> getSignRuleIdList(SignPattern signPattern) {
		String key = StringManager.formatKeyString(SIGNRULE, signPattern.getCountyCode().toString());
		if (needFillCache(key)) {
			List<Integer> idLists = signRuleMapper.listSignRuleId(signPattern.getCountyCode(), signPattern.getId());
			if (CollectionUtils.isEmpty(idLists)) {
				redisCache.setEmpty(key);
				return Collections.EMPTY_LIST;
			}
			redisCache.listRpush(key, idLists.stream().map(String::valueOf).toArray(String[]::new));
			return idLists;
		}
		List<String> result = redisCache.listLrange(key, 0, -1);
		if (CollectionUtils.isEmpty(result))
			return Collections.EMPTY_LIST;
		return result.stream().map(Ints::tryParse).collect(Collectors.toList());
	}
	
	/**
	 * 根据签到规则ID 查询出具体的签到规则
	 * @param signRuleId
	 * @return
	 */
	public SignRule getSignRuleFromCache(Integer signRuleId){
		SignRule signRule = redisCache.hmget(signRuleId.toString(),SignRule.class);
		if (signRule != null)
            return signRule;
        if (signRule == null && !redisCache.isEmpty(signRuleId.toString(), SignRule.class)) {
        	signRule = signRuleMapper.selectByPrimaryKey(signRuleId);
            if (signRule == null) {
                redisCache.setEmpty(signRuleId.toString(), SignRule.class);
                return null;
            }
            redisCache.hmset(signRule.getId().toString(), signRule);
        }
		return signRule;
	}
	
	/**
	 * 根据签到方式ID 查询出具体的签到方式
	 * @param signPatternId
	 * @return
	 */
	public SignPattern getSignPatternFromCache(Integer signPatternId){
		SignPattern signPattern = redisCache.hmget(signPatternId.toString(),SignPattern.class);
		if (signPattern != null)
            return signPattern;
        if (signPattern == null && !redisCache.isEmpty(signPatternId.toString(), SignPattern.class)) {
        	signPattern = signPatternMapper.selectByPrimaryKey(signPatternId);
            if (signPattern == null) {
                redisCache.setEmpty(signPatternId.toString(), SignPattern.class);
                return null;
            }
            redisCache.hmset(signPattern.getId().toString(), signPattern);
        }
		return signPattern;
	}

	/**
	 * 保存编辑签到设置
	 * @param signPattern
	 * @param signRules
	 */
	public void modifySignConfigInfo(SignPattern signPattern, List<SignRule> signRules) {
		signPattern.setSignStatus(SignPattern.EFFECTIVE);
		signPatternMapper.updateByPrimaryKeySelective(signPattern);
		SignPattern otherSignPattern = new SignPattern();
		otherSignPattern.setSignStatus(SignPattern.INEFFECTIVE);
		otherSignPattern.setCountyCode(signPattern.getCountyCode());
		otherSignPattern.setId(signPattern.getId());
		signPatternMapper.updateOtherSignPattern(otherSignPattern);
		signRuleMapper.batchUpdate(signRules);
		deleteSignCache(signPattern,signRules);
	}
	
	private void deleteSignCache(SignPattern signPattern,List<SignRule> signRules){
		String signPatternKey = StringManager.formatKeyString(SIGNPATTERN_EFFECTIVE,signPattern.getCountyCode().toString());
		String signRulekey = StringManager.formatKeyString(SIGNRULE, signPattern.getCountyCode().toString());
		redisCache.delete(signPatternKey);
		redisCache.delete(signPattern.getId().toString(), SignPattern.class);
		redisCache.delete(signRulekey);
		signRules.stream().forEach((rule)->redisCache.delete(rule.getId().toString(), SignRule.class));
	}
	
	/**
	 * 根据索引从缓存中获取签到规则
	 * @param countyCode
	 * @param index
	 * @return
	 */
	public SignRule getSignRuleByIndex(Integer countyCode,long index){
		SignPattern signPattern = getEfficientSignPattern(countyCode);
		Preconditions.checkArgument(signPattern != null, "当前地市没有初始有效签到方式信息数据！");
		getSignRuleIdList(signPattern);
		String key = StringManager.formatKeyString(SIGNRULE, countyCode.toString());
		String signRuleId = redisCache.lindex(key, index-1);
		Preconditions.checkArgument(StringUtils.isNotEmpty(signRuleId), "当前地市没有初始签到规则信息数据！");
		return getSignRuleFromCache(Integer.valueOf(signRuleId));
	}
	
	/**
	 * 判断是否用户是否已经签到
	 * true 已签 false 未签
	 * @param userId
	 * @param countyCode
	 * @param localDateStr 如20171024
	 * @return true 已签  false 未签
	 */
	public boolean signed(Integer userId,Integer countyCode,String localDateStr){
		String key = StringManager.formatKeyString(SIGNEDORNOT, countyCode.toString(),localDateStr);
		if(needFillCache(key)){
			List<Integer> userIds = signRecordMapper.listSignedUser(countyCode, localDateStr);
			if(CollectionUtils.isEmpty(userIds)){
				return false;
			}
			redisCache.sadd(key, userIds.stream().map(String::valueOf).toArray(String[]::new));
		}
		return redisCache.sismember(key, userId.toString());
	}
	
	/**
	 * 签到完成之后将用户加入到已签集合中
	 * @param userId
	 * @param countyCode
	 * @param lastDate
	 */
	public void addSignedUser(Integer userId,Integer countyCode,String lastDate){
		String key = StringManager.formatKeyString(SIGNEDORNOT, countyCode.toString(),lastDate);
		redisCache.sadd(key, userId.toString());
	}
	
	/**
	 * 将从聚合接口拿到的万年历信息保存到缓存中
	 * @param specifyDate
	 * @param map
	 * @return
	 */
	public SignCommonInfoVO getSignCommonInfo(String specifyDate,Map<String,Object> map) {
		pullAlmanacInfo(specifyDate,0);
		String key = StringManager.formatKeyString(SIGNCOMMONINFO,specifyDate);
		SignCommonInfo source = redisCache.hmget(key, SignCommonInfo.class);
		System.out.println("The source>>>>>>>>>>>>>>>>>>>>>>"+JSON.toJSON(source));
		SignCommonInfoVO target = SignCommonInfoVO.convertFrom(source);
		map.put("errorCode", source.getErrorCode());
		map.put("reason",source.getReason());
		return target;
	}
	
	/**
	 * @param specifyDate
	 * @param index
	 * @return
	 */
	public boolean pullAlmanacInfo(String specifyDate,Integer index) {
		String key = StringManager.formatKeyString(SIGNCOMMONINFO,specifyDate);
		String wholeKey = SignCommonInfo.class.getName()+":"+key;
		if(needFillCache(wholeKey)){
			Map<String, Object> params = new HashMap<>();
			params.put("key", APPKEY);
			params.put("date",specifyDate);
			String result = HttpClientUtil.postRequest(URL, params);
			JSONObject object = JSONObject.parseObject(result);
			SignCommonInfo signCommonInfo = new SignCommonInfo();
			if (object.getIntValue("error_code") == 0) {
				signCommonInfo = object.getJSONObject("result").getObject("data", SignCommonInfo.class);
				signCommonInfo = signCommonInfo.changeSelf(signCommonInfo);
				AlmanacInfo almanacInfo = new AlmanacInfo();
				almanacInfo.setSpecifyDate(specifyDate);
				BeanUtils.copyProperties(signCommonInfo, almanacInfo);
				almanacInfoMapper.saveOrUpdate(almanacInfo);
			}
			signCommonInfo.setErrorCode(object.getIntValue("error_code"));
			signCommonInfo.setReason(object.get("reason").toString());
			redisCache.hmset(key, signCommonInfo,(index+1)*expire);
			return true;
		}
		return false;
	}
	
	/**
	 * 获取用户的签到记录信息
	 * @param countyCode
	 * @param lastDate
	 * @param userId
	 * @return
	 */
	public SignRecord getSignRecordFromCache(Integer countyCode,Integer userId) {
		String key = StringManager.formatKeyString(SIGNRECORD,countyCode.toString(),userId.toString());
		String wholeKey = SignRecord.class.getName()+":"+key;
		if(needFillCache(wholeKey)){
			SignRecord record = new SignRecord();
			record.setCountyCode(countyCode);
			record.setUserid(userId);
			SignRecord result = signRecordMapper.selectRecordBy(record);
			if(result == null){
				return null;
			}
			redisCache.hmset(key, result);
		}
		return redisCache.hmget(key, SignRecord.class);
	}

	/**
	 * 判断是否是连续登陆
	 * @param lastSignDate
	 * @return
	 */
	private boolean isSerialSignIn(String lastDate,LocalDate localDate){
		LocalDate lastSignDate = LocalDate.parse(lastDate,DateTimeFormatter.BASIC_ISO_DATE);
		return lastSignDate.plusDays(1).isEqual(localDate);
	}
	
	public void checkSignRecordFormInsert(Integer countyCode, Integer userId,LocalDate localDate) {
		String localDateInsert = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
		SignRecord insertRecord = new SignRecord();
		SignRecord signRecord = getSignRecordFromCache(countyCode,userId);
		SignPattern signPattern = getEfficientSignPattern(countyCode);
		int serialdays = 1;
		int signDays = 1;
		if(signRecord != null){
			String lastDate = signRecord.getLastdate();
			if(isSerialSignIn(lastDate,localDate)){
				serialdays = signRecord.getSerialdays()+1;
				//判断连续签到下此时的签到方式是否和上次的签到方式一样，若一样则这部分的连续天数继续加1
				int lastSignType = signRecord.getSignType();
				if(Objects.equals(lastSignType, signPattern.getSignType())){
					signDays = signRecord.getSignDays()+1;
				}
			}
		}
		insertRecord.setUserid(userId);
		insertRecord.setLastdate(localDateInsert);
		insertRecord.setSerialdays(serialdays);
		insertRecord.setSignType(signPattern.getSignType());
		insertRecord.setSignDays(signDays);
		insertRecord.setCountyCode(countyCode);
		signRecordMapper.saveOrUpdate(insertRecord);
	}
	
	/**
	 * 签到完成后的对缓存的相关操作
	 * @param countyCode
	 * @param userId
	 * @param localDateInsert
	 */
	public void operateSignRecord(Integer countyCode, Integer userId,String localDateInsert){
		String key = StringManager.formatKeyString(SIGNRECORD,countyCode.toString(),userId.toString());
		redisCache.delete(key, SignRecord.class);
		addSignedUser(userId, countyCode, localDateInsert);
	}
}
