package org.zclpro.db.constellatory.impl;

import java.util.List;

import org.zclpro.db.constellatory.entity.ConstellatoryDay;

public interface ConstellatoryDayMapper {
	
    int insert(ConstellatoryDay record);

    int insertSelective(ConstellatoryDay record);
    
    int saveOrUpdate(ConstellatoryDay record);
    
    int batchSaveOrUpdate(List<ConstellatoryDay> list);
}