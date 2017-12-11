package org.zclpro.db.grade.impl;

import java.util.List;

import org.zclpro.db.grade.entity.GradeDistribution;

public interface GradeDistributionMapper {
	int saveOrUpdate(GradeDistribution record);

	List<GradeDistribution> queryGradeDistribution(GradeDistribution record);
}