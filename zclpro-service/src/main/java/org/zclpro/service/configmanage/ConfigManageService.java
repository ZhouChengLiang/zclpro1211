package org.zclpro.service.configmanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zclpro.db.configmanage.entity.ConfigManage;
import org.zclpro.db.configmanage.impl.ConfigManageMapper;

import com.github.pagehelper.Page;
import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConfigManageService {
	
	@Autowired
	private ConfigManageCache configManageCache;
	
	@Autowired
	private ConfigManageMapper configManageMapper;
	
	public Page<ConfigManage> getConfigs(ConfigManage config){
		Page<ConfigManage> configs = configManageMapper.selectAll(config);
		return configs;
	}

    @Transactional(rollbackFor = Exception.class)
    public void toggle(ConfigManage config) {
		ConfigManage oldConfig = configManageCache.getConfig(config.getId());
		config.setConfigStatus(oldConfig.getConfigStatus());
		configManageMapper.toggle(config);
		configManageCache.delConfig_wrap(oldConfig);
	}

	public ConfigManage getConfig(ConfigManage config){
		ConfigManage singleConfig = configManageCache.getConfig(config.getId());
		Preconditions.checkArgument(singleConfig != null, "当前地市没有初始"+ConfigManage.ConfigNameEnum.getByType(singleConfig.getConfigType()).getName()+"开关配置数据！");
		return singleConfig;
	}
	
	/**
	 * 获取关于用户等级的开关状态
	 * @param countyCode
	 * @return
	 */
	public ConfigManage getGradeInfoConfig(Integer countyCode){
		return getConfigBackUp(ConfigManage.GRADE,countyCode);
	}
	
	/**
	 * 获取关于签到设置的开关状态
	 * @param countyCode
	 * @return
	 */
	public ConfigManage getSignConfig(Integer countyCode){
		return getConfigBackUp(ConfigManage.SIGNIN,countyCode);
	}
	
	/**
	 * 根据功能配置类型,县编码来获取配置项状态
	 * @param configType
	 * @param countyCode
	 * @return
	 */
	public ConfigManage getConfigBackUp(Integer configType,Integer countyCode){
		ConfigManage singleConfig = configManageCache.getConfigBackUp(configType, countyCode);
		Preconditions.checkArgument(singleConfig != null, "当前地市没有初始"+ConfigManage.ConfigNameEnum.getByType(configType).getName()+"开关配置数据！");
		return singleConfig;
	}
}
