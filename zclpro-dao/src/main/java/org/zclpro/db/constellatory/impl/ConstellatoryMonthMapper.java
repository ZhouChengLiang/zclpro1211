package org.zclpro.db.constellatory.impl;

import java.util.List;

import org.zclpro.db.constellatory.entity.ConstellatoryMonth;

public interface ConstellatoryMonthMapper {
    int insert(ConstellatoryMonth record);

    int insertSelective(ConstellatoryMonth record);
    
    int saveOrUpdate(ConstellatoryMonth record);
    
    int batchSaveOrUpdate(List<ConstellatoryMonth> list);
}