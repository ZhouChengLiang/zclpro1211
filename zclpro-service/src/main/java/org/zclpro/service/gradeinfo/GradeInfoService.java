package org.zclpro.service.gradeinfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.zclpro.db.configmanage.entity.ConfigManage;
import org.zclpro.db.grade.entity.GradeDistribution;
import org.zclpro.db.grade.entity.GradeInfo;
import org.zclpro.db.grade.impl.GradeDistributionMapper;
import org.zclpro.db.grade.impl.GradeInfoMapper;
import org.zclpro.db.userexpint.entity.UserExpInt;
import org.zclpro.db.userexpint.impl.UserExpIntMapper;
import org.zclpro.service.configmanage.ConfigManageCache;
import org.zclpro.service.configmanage.ConfigManageVO;

import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GradeInfoService {
	
	@Autowired
	private GradeInfoCache gradeInfoCache;
	
	@Autowired
	private ConfigManageCache configManageCache;
	
	@Autowired
	private GradeInfoMapper gradeInfoMapper;
	
	@Autowired
	private GradeDistributionMapper gradeDistributionMapper;
	
	@Autowired
	private UserExpIntMapper userExpIntMapper;
	/**
	 * 提供信息用户成长等级管理页面
	 * @param countyCode
	 * @return
	 */
	public Map<String, Object> getGradeInfos(Integer countyCode){
		Map<String, Object> result = new HashMap<>();
		ConfigManage source =  configManageCache.getConfigBackUp(ConfigManage.GRADE, countyCode);
		Preconditions.checkArgument(source != null, "当前地市没有初始成长等级开关配置数据！");
		ConfigManageVO gradeConfig = ConfigManageVO.convertFrom(source);
		result.put("gradeConfig", gradeConfig);
		List<GradeInfoVO> gradeInfo = getGradeInfosByCountyCode(countyCode);
		result.put("gradeInfo", gradeInfo);
		return result;
	}
	
	/**
	 * 获取地市下成长等级信息
	 * @param countyCode
	 * @return
	 */
	public List<GradeInfoVO> getGradeInfosByCountyCode(Integer countyCode){
		List<GradeInfoVO> result = new ArrayList<>();
		List<Integer> idList = gradeInfoCache.getGradeInfosByCountyCode(countyCode);
		if (CollectionUtils.isEmpty(idList))
            return Collections.EMPTY_LIST;
		for(Integer gradeInfoId:idList){
			GradeInfo source = gradeInfoCache.getGradeInfoFromCache(gradeInfoId);
			GradeInfoVO gradeInfoVO = GradeInfoVO.convertFromAtWeb(source);
			result.add(gradeInfoVO);
		}
		return result;
	}
	
	/**
	 * 根据经验值从缓存中查询出当前属于哪一个等级
	 * @param curExperience
	 * @param countyCode
	 * @return
	 */
	public GradeInfo getCurrentGradeInfo(Integer curExperience,Integer countyCode){
		return gradeInfoCache.getSuitableGradeInfo(curExperience,countyCode,GradeInfo.CURGRADEINFO);
	}
	
	/**
	 * 根据经验值 查出符合的下一级等级信息
	 * @param curExperience
	 * @param countyCode
	 * @return
	 */
	public GradeInfo getNextGradeInfo(Integer curExperience,Integer countyCode){
		return gradeInfoCache.getSuitableGradeInfo(curExperience,countyCode,GradeInfo.NEXTGRADEINFO);
	}
	
    /**
     * 单行编辑保存成长等级信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifySingleGradeInfo(GradeInfo gradeInfo){
		gradeInfoMapper.updateByPrimaryKeySelective(gradeInfo);
		gradeInfoCache.delGradeInfoSingle_wrap(gradeInfo.getId());
	}
    
	/**
	 * 提供给定时任务用来更新各等级下的人数
	 */
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
	public void calculateGradeInfo(){
    	Date curtime = new Date();
		String curdate = DateFormatUtils.format(curtime, "yyyyMMdd");
		List<UserExpInt> userExpIntList = userExpIntMapper.selectByCountyCode(null);
		Map<Integer,Map<GradeInfo,List<UserExpInt>>> map = userExpIntList.stream()
								.collect(Collectors.groupingBy(UserExpInt::getCountyCode,
										Collectors.groupingBy(e->getCurrentGradeInfo(e.getExperience(),e.getCountyCode()))));
		for(Map.Entry<Integer, Map<GradeInfo,List<UserExpInt>>> entry:map.entrySet()){
			Integer curCountyCode = entry.getKey().intValue();
			List<Integer> ids = gradeInfoCache.getGradeInfosByCountyCode(curCountyCode);
			if(!CollectionUtils.isEmpty(ids)){
				gradeInfoMapper.batchUpdateByIds(ids);
				ids.stream().forEach(
						(id)->{
							GradeDistribution record = new GradeDistribution();
							record.setCountyCode(curCountyCode);
							record.setCurdate(curdate);
							record.setCurexp(gradeInfoCache.getGradeInfoFromCache(id).getRequiredExperience());
							record.setCurusers(0);
							record.setCurtime(curtime);
							log.error("At step 1 curGradeInfo.getId()>>>>>>>>>>>>>>>>>>>>"+id);
							record.setCurgrade(id);
							gradeDistributionMapper.saveOrUpdate(record);
							}
						);
			}
			Map<GradeInfo,List<UserExpInt>> innerMap = entry.getValue();
			for(Map.Entry<GradeInfo,List<UserExpInt>> innerEntry:innerMap.entrySet()){
				GradeInfo curGradeInfo = innerEntry.getKey();
				log.error("Take attention GradeInfo>>>>>>>>>>>>>>>>>>>>"+curGradeInfo);
				log.error("At step 2 curGradeInfo.getId()>>>>>>>>>>>>>>>>>>>>"+curGradeInfo.getId());
				Integer curUsers = innerEntry.getValue().size();
				GradeDistribution record = new GradeDistribution();
				Integer curgrade = curGradeInfo.getId();
				Integer curexp = curGradeInfo.getRequiredExperience();
				curGradeInfo.setCurrentUsers(curUsers);
				record.setCountyCode(curCountyCode);
				record.setCurdate(curdate);
				record.setCurexp(curexp);
				record.setCurusers(curUsers);
				record.setCurgrade(curgrade);
				record.setCurtime(curtime);
				gradeInfoMapper.updateByPrimaryKeySelective(curGradeInfo);
				gradeDistributionMapper.saveOrUpdate(record);
			}
			gradeInfoCache.delGradeInfo_wrap(ids);
		}
	}
    
    /**
     * 新的方法生成等级人员分布情况
     */
    @Transactional(rollbackFor = Exception.class)
	public void generateGradeDistribution(){
    	Date curtime = new Date();
		String curdate = DateFormatUtils.format(curtime, "yyyyMMdd");
		List<GradeDistribution> gradeDistributions = gradeDistributionMapper.calcGradeDistribution();
		Map<Integer,List<GradeDistribution>> map = gradeDistributions.stream().collect(Collectors.groupingBy(GradeDistribution::getCountyCode));
		for(Map.Entry<Integer, List<GradeDistribution>> entry:map.entrySet()){
			Integer curCountyCode = entry.getKey().intValue();
			List<Integer> ids = gradeInfoCache.getGradeInfosByCountyCode(curCountyCode);
			if(!CollectionUtils.isEmpty(ids)){
				gradeInfoMapper.batchUpdateByIds(ids);
				List<GradeDistribution> list = new ArrayList<>();
				ids.forEach(
						(id)->{
								GradeDistribution record = new GradeDistribution();
								record.setCountyCode(curCountyCode);
								record.setCurdate(curdate);
								record.setCurexp(gradeInfoCache.getGradeInfoFromCache(id).getRequiredExperience());
								record.setCurusers(0);
								record.setCurtime(curtime);
								record.setCurgrade(id);
								list.add(record);
							}
						);
				gradeDistributionMapper.batchSaveOrUpdate(list);
			}
			List<GradeDistribution> result = entry.getValue(); 
			for(GradeDistribution gd :result){
				GradeInfo curGradeInfo = gradeInfoCache.getGradeInfoFromCache(gd.getCurgrade());
				curGradeInfo.setCurrentUsers(gd.getCurusers());
				gd.setCurdate(curdate);
				gd.setCurexp(curGradeInfo.getRequiredExperience());
				gd.setCurtime(curtime);
				gradeInfoMapper.updateByPrimaryKeySelective(curGradeInfo);
			}
			gradeDistributionMapper.batchSaveOrUpdate(result);
			gradeInfoCache.delGradeInfo_wrap(ids);
		}
	}
    
    /**
     * 最最新的计算等级分布
     */
	public void generateGradeDistribution_last(){
    	Date curtime = new Date();
		String curdate = DateFormatUtils.format(curtime, "yyyyMMdd");
		List<GradeDistribution> gradeDistributions = gradeDistributionMapper.calcGradeDistribution();
		Map<Integer,List<GradeDistribution>> map = gradeDistributions.stream().collect(Collectors.groupingBy(GradeDistribution::getCountyCode));
		List<GradeDistribution> list = new ArrayList<>();
		List<Integer> idsList = new ArrayList<>();
		for(Map.Entry<Integer, List<GradeDistribution>> entry:map.entrySet()){
			Integer curCountyCode = entry.getKey().intValue();
			List<Integer> ids = gradeInfoCache.getGradeInfosByCountyCode(curCountyCode);
			if(!CollectionUtils.isEmpty(ids)){
				gradeInfoMapper.batchUpdateByIds(ids);
				idsList.addAll(ids);
				ids.forEach(
						(id)->{
								GradeDistribution record = new GradeDistribution();
								record.setCountyCode(curCountyCode);
								record.setCurdate(curdate);
								record.setCurexp(gradeInfoCache.getGradeInfoFromCache(id).getRequiredExperience());
								record.setCurusers(0);
								record.setCurtime(curtime);
								record.setCurgrade(id);
								list.add(record);
							}
						);
			}
			List<GradeDistribution> result = entry.getValue(); 
			for(GradeDistribution gd :result){
				GradeInfo curGradeInfo = gradeInfoCache.getGradeInfoFromCache(gd.getCurgrade());
				Integer curexp = curGradeInfo.getRequiredExperience();
				curGradeInfo.setCurrentUsers(gd.getCurusers());
				gd.setCurdate(curdate);
				gd.setCurexp(curexp);
				gd.setCurtime(curtime);
				gradeInfoMapper.updateByPrimaryKeySelective(curGradeInfo);
			}
			list.addAll(result);
		}
		if(!CollectionUtils.isEmpty(list)){
			gradeDistributionMapper.deleteByCondition(curdate);
			gradeDistributionMapper.batchSaveOrUpdate(list);
			gradeInfoCache.delGradeInfo_wrap(idsList);
		}
	}
}
