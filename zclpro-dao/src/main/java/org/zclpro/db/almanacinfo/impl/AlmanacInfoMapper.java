package org.zclpro.db.almanacinfo.impl;

import org.zclpro.db.almanacinfo.entity.AlmanacInfo;

public interface AlmanacInfoMapper {
    int insertSelective(AlmanacInfo record);
    
    int saveOrUpdate(AlmanacInfo record);
}