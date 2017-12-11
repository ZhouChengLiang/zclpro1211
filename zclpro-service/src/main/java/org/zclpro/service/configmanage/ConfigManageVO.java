package org.zclpro.service.configmanage;

import org.springframework.beans.BeanUtils;
import org.zclpro.db.configmanage.entity.ConfigManage;

import lombok.Data;

@Data
public class ConfigManageVO {
	private Integer id;
	private Integer configStatus;
	
	public static ConfigManageVO convertFrom(ConfigManage source){
		ConfigManageVO target = new ConfigManageVO();
		BeanUtils.copyProperties(source, target);
		return target;
	}
}
