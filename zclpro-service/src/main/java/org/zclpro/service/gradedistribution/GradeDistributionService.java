package org.zclpro.service.gradedistribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.zclpro.db.grade.entity.GradeDistribution;
import org.zclpro.db.grade.entity.GradeInfo;
import org.zclpro.db.grade.impl.GradeDistributionMapper;
import org.zclpro.service.gradeinfo.GradeInfoCache;

@Service
public class GradeDistributionService {
	
	@Autowired
	private GradeDistributionMapper gradeDistributionMapper;
	
	@Autowired
	private GradeInfoCache gradeInfoCache;
	
	/**
	 * 如果现在的等级标准与之前的等级标准一样,则说明级别没有调整过
	 * 如果当前的等级与之前的等级不一样则把之前的等级的人数加到现在的等级人数上  以上适用于 当前等级比以前的等级少了的情况
	 * @param record
	 * @return
	 */
	public List<GradeDistributionVO> getGradeDistribution(GradeDistribution record) {
		List<GradeDistribution> gradeDistributions = gradeDistributionMapper.queryGradeDistribution(record);
		Map<String,List<GradeDistribution>> firstGroupBy = gradeDistributions.stream().collect(Collectors.groupingBy
				(GradeDistribution::getCurdate,TreeMap::new,Collectors.toList()));
		List<GradeDistributionVO> voList = new ArrayList<>();
		for(Map.Entry<String, List<GradeDistribution>> entry:firstGroupBy.entrySet()){
			Map<Integer,Integer> innerMap = initMapWithCurrentGradeInfos(record.getCountyCode());
			for(GradeDistribution gd:entry.getValue()){
				Integer curexp = gd.getCurexp();
				Integer countyCode = gd.getCountyCode();
				Integer curusers = gd.getCurusers();
				Integer eldergrade = gd.getCurgrade();
				Integer curgrade = gradeInfoCache.getSuitableGradeInfo(curexp,countyCode,GradeInfo.CURGRADEINFO).getId();//现在的等级
				if(Objects.equals(eldergrade, curgrade)){
					innerMap.put(curgrade, curusers);
				}else{
					innerMap.put(curgrade, innerMap.get(curgrade)+curusers);
				}
			}
			GradeDistributionVO vo = new GradeDistributionVO();
			vo.setDate(entry.getKey());
			vo.setList(innerMap.values());
			vo = GradeDistributionVO.changeSelf(vo);
			voList.add(vo);
		}
		return voList;
	}
	
	private Map<Integer,Integer> initMapWithCurrentGradeInfos(Integer countyCode){
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		List<Integer> ids = gradeInfoCache.getGradeInfosByCountyCode(countyCode);
		if(!CollectionUtils.isEmpty(ids)){
			ids.stream().forEach((id)->map.put(id, 0));
		}
		return map;
	}
}
