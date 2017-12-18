package org.zclpro.db.userexpint.impl;

import java.util.List;

import org.zclpro.db.userexpint.entity.UserExpInt;

public interface UserExpIntMapper {
    int insert(UserExpInt record);

    int insertSelective(UserExpInt record);
    
    int batchSaveOrUpdate(List<UserExpInt> list);
}