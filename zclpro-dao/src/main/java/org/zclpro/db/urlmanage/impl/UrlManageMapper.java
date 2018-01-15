package org.zclpro.db.urlmanage.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zclpro.db.urlmanage.entity.UrlManage;

public interface UrlManageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UrlManage record);

    int insertSelective(UrlManage record);

    UrlManage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UrlManage record);

    int updateByPrimaryKey(UrlManage record);
    
    List<UrlManage> selectByCountyCode(@Param("countyCode") Integer countyCode);
}