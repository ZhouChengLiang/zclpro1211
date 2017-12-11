package org.zclpro.db.sign.impl;

import org.zclpro.db.sign.entity.SignPattern;

public interface SignPatternMapper {
	int insertSelective(SignPattern signPattern);

    SignPattern selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SignPattern signPattern);
    
    SignPattern selectByCondition(SignPattern signPattern);

	void updateOtherSignPattern(SignPattern otherSignPattern);
}