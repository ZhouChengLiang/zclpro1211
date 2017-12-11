package org.zclpro.db.sign.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zclpro.db.sign.entity.SignRule;

public interface SignRuleMapper {
	int insertSelective(SignRule record);

    SignRule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SignRule record);
    
    List<Integer> listSignRuleId(@Param("countyCode") Integer countyCode,@Param("signPattern") Integer signPattern);
    
    void batchUpdate(List<SignRule> list);
}