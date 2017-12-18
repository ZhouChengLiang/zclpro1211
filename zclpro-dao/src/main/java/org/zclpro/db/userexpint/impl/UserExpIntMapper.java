package org.zclpro.db.userexpint.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zclpro.db.userexpint.entity.UserExpInt;

public interface UserExpIntMapper {
    int insert(UserExpInt record);

    int insertSelective(UserExpInt record);
    
    int batchSaveOrUpdate(List<UserExpInt> list);
    
    List<UserExpInt> selectByCountyCode(@Param("countyCode") Integer countyCode);
}