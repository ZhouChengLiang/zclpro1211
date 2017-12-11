package org.zclpro.db.grade.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zclpro.db.grade.entity.GradeInfo;
import org.zclpro.db.grade.entity.GradeInfoKeySortNum;

public interface GradeInfoMapper {
	int insertSelective(GradeInfo record);

	GradeInfo selectByPrimaryKey(Integer id);

	List<GradeInfo> select(GradeInfo record);

	void batchUpdate(List<GradeInfo> list);

	List<GradeInfoKeySortNum> listGradeInfoKeySort(@Param("countyCode") Integer countyCode);

	void batchUpdateByIds(List<Integer> list);

	int updateByPrimaryKeySelective(GradeInfo record);

}