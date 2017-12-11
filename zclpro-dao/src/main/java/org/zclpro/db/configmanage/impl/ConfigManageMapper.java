package org.zclpro.db.configmanage.impl;

import org.apache.ibatis.annotations.Param;
import org.zclpro.db.configmanage.entity.ConfigManage;

import com.github.pagehelper.Page;

public interface ConfigManageMapper {
	int insertSelective(ConfigManage record);

    int updateByPrimaryKeySelective(ConfigManage record);
    
    //以下代码实际使用到
    ConfigManage selectByPrimaryKey(Integer id);
    
    Page<ConfigManage> selectAll(ConfigManage config);
    
	void toggle(ConfigManage config);
	
	ConfigManage selectConfigBy(@Param("configType") Integer configType,@Param("countyCode") Integer countyCode);
}