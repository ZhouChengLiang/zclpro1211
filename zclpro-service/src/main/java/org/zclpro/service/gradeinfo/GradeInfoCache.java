package org.zclpro.service.gradeinfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.zclpro.common.propertiestool.StringManager;
import org.zclpro.db.grade.entity.GradeInfo;
import org.zclpro.db.grade.entity.GradeInfoKeySortNum;
import org.zclpro.db.grade.impl.GradeInfoMapper;
import org.zclpro.service.common.BaseCache;
import org.zclpro.service.common.Constants;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Tuple;

@Service
@Slf4j
public class GradeInfoCache extends BaseCache{
	
	@Autowired
	private GradeInfoMapper gradeInfoMapper;
	
	private final static StringManager stringManager = StringManager.getStringManageByFileName(Constants.GRADEINFO_PROPERTIES_PATH);
	
	private int expire = stringManager.getIntValue("gradeinfo_expire");
	
	private final static String GRADE_INFOS = stringManager.getValue("gradeinfos");
	
	@Override
	public int getExpire() {
		return expire;
	}

	public List<Integer> getGradeInfosByCountyCode(Integer countyCode) {
		String key = StringManager.formatKeyString(GRADE_INFOS, countyCode.toString());
		if (needFillCache(key)) {
			List<GradeInfoKeySortNum> keySortNumList = gradeInfoMapper.listGradeInfoKeySort(countyCode);
			if (CollectionUtils.isEmpty(keySortNumList)) {
				redisCache.setEmpty(key);
				return Collections.EMPTY_LIST;
			}
			Map<String, Double> keySortMap = new HashMap<>();
			for (GradeInfoKeySortNum each : keySortNumList) {
				keySortMap.put(String.valueOf(each.getId()), (double) each.getRequiredExperience());
			}
			redisCache.zadd(key, keySortMap);
			redisCache.expire(key, expire);
		}
		Set<String> result = redisCache.zrangeByScore(key, Long.MIN_VALUE, Long.MAX_VALUE);
        if (CollectionUtils.isEmpty(result))
            return Collections.EMPTY_LIST;
        return result.stream().map(Ints::tryParse).collect(Collectors.toList());
	}
	
	public GradeInfo getGradeInfoFromCache(Integer gradeInfoId){
		GradeInfo gradeInfo = redisCache.hmget(gradeInfoId.toString(),GradeInfo.class);
		if (gradeInfo != null)
            return gradeInfo;
        if (gradeInfo == null && !redisCache.isEmpty(gradeInfoId.toString(), GradeInfo.class)) {
        	gradeInfo = gradeInfoMapper.selectByPrimaryKey(gradeInfoId);
            if (gradeInfo == null) {
                redisCache.setEmpty(gradeInfoId.toString(), GradeInfo.class);
                return null;
            }
            redisCache.hmset(gradeInfo.getId().toString(), gradeInfo,expire);
        }
		return gradeInfo;
	}
	
	/**
	 * 根据经验值换算出符合的等级信息
	 * @param curExperience
	 * @param countyCode
	 * @param condition GradeInfo.NEXTGRADEINFO  GradeInfo.CURGRADEINFO  
	 * @return
	 */
	public GradeInfo getSuitableGradeInfo(Integer curExperience, Integer countyCode,String condition){
		Preconditions.checkArgument(curExperience!=null, "当前经验值为空!");
		String key = StringManager.formatKeyString(GRADE_INFOS, countyCode.toString());
		getGradeInfosByCountyCode(countyCode);
		Set<Tuple> set;
		if(Objects.equal(condition, GradeInfo.CURGRADEINFO)){
			set = redisCache.zrevrangeByScoreWithScores(key, curExperience, 0, 0, 1);
		}else{
			curExperience = Integer.sum(curExperience, 1);
			set = redisCache.zrangeByScoreWithScores(key,curExperience, Double.MAX_VALUE, 0, 1);
			if(CollectionUtils.isEmpty(set)){
				set = redisCache.zrevrangeByScoreWithScores(key, curExperience, 0, 0, 1);
			}
		}
		Iterator<Tuple> it = set.iterator();
		Integer gradeInfoId = null;
		while(it.hasNext()){
			gradeInfoId = Integer.valueOf(it.next().getElement());
		}
		Preconditions.checkArgument(gradeInfoId!=null, "当前地市没有初始等级信息数据！");
		return getGradeInfoFromCache(gradeInfoId);
	}
	
	public void delGradeInfo_wrap(List<Integer> list){
		Iterator<Integer> it = list.iterator();
		while(it.hasNext()){
			redisCache.delete(it.next().toString(), GradeInfo.class);
		}
	}
	
	public void delGradeInfoSingle_wrap(Integer id){
		redisCache.delete(id.toString(), GradeInfo.class);
	}
	
}
