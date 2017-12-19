package org.zclpro.db.grade.impl;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.zclpro.db.grade.entity.GradeDistribution;

public interface GradeDistributionMapper {
	int saveOrUpdate(GradeDistribution record);

	List<GradeDistribution> queryGradeDistribution(GradeDistribution record);
	
	List<GradeDistribution> calcGradeDistribution();
	
	int batchSaveOrUpdate(List<GradeDistribution> list);
	
	@Delete("DELETE FROM grade_distribution WHERE curdate = #{curdate}")
	int deleteByCondition(@Param("curdate")String curdate);
	
	int batchInsert(List<GradeDistribution> list);
}