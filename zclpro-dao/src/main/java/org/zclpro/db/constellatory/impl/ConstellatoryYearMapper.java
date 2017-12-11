package org.zclpro.db.constellatory.impl;

import java.util.List;

import org.zclpro.db.constellatory.entity.ConstellatoryYear;

public interface ConstellatoryYearMapper {
    int insert(ConstellatoryYear record);

    int insertSelective(ConstellatoryYear record);
    
    int saveOrUpdate(ConstellatoryYear record);
    
    int batchSaveOrUpdate(List<ConstellatoryYear> list);
}