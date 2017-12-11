package org.zclpro.db.constellatory.impl;

import java.util.List;

import org.zclpro.db.constellatory.entity.ConstellatoryWeek;

public interface ConstellatoryWeekMapper {
    int insert(ConstellatoryWeek record);

    int insertSelective(ConstellatoryWeek record);
    
    int saveOrUpdate(ConstellatoryWeek record);
    
    int batchSaveOrUpdate(List<ConstellatoryWeek> list);
}