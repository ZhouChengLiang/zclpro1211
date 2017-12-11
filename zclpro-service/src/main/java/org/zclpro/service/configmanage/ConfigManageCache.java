package org.zclpro.service.configmanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zclpro.common.propertiestool.StringManager;
import org.zclpro.db.configmanage.entity.ConfigManage;
import org.zclpro.db.configmanage.impl.ConfigManageMapper;
import org.zclpro.service.common.BaseCache;
import org.zclpro.service.common.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConfigManageCache extends BaseCache{
	
	@Autowired
	private ConfigManageMapper configManageMapper;
	
	private final static StringManager stringManager = StringManager.getStringManageByFileName(Constants.CONFIGMANAGE_PROPERTIES_PATH);

    private final static String CONFIG_BACKUP = stringManager.getValue("configbackup");
    
	public ConfigManage getConfig(Integer id){
		ConfigManage config = redisCache.hmget(id.toString(),ConfigManage.class);
		if (config != null)
            return config;
        if (config == null && !redisCache.isEmpty(id.toString(), ConfigManage.class)) {
            config = configManageMapper.selectByPrimaryKey(id);
            if (config == null) {
                redisCache.setEmpty(id.toString(), ConfigManage.class);
                return null;
            }
            addConfig_wrap(config);
        }
		return config;
	}
	
	public ConfigManage getConfigBackUp(Integer configType,Integer countyCode){
		String key = StringManager.formatKeyString(CONFIG_BACKUP,configType.toString(),countyCode.toString());
        ConfigManage config = redisCache.hmget(key,ConfigManage.class);
        if (config != null)
            return config;
        if (config == null && !redisCache.isEmpty(key, ConfigManage.class)) {
            config = configManageMapper.selectConfigBy(configType,countyCode);
            if (config == null) {
                redisCache.setEmpty(key, ConfigManage.class);
                return null;
            }
            addConfig_wrap(config);
        }
        return config;
	}
	
	public void addConfig_wrap(ConfigManage config){
		String key_backup = StringManager.formatKeyString(CONFIG_BACKUP,config.getConfigType().toString(),config.getCountyCode().toString());
        String key = config.getId().toString();
		redisCache.hmset(key, config);
		redisCache.hmset(key_backup, config);
	}
	
	public void delConfig_wrap(ConfigManage config){
		String key_backup = StringManager.formatKeyString(CONFIG_BACKUP,config.getConfigType().toString(),config.getCountyCode().toString());
		String key = config.getId().toString();
		redisCache.delete(key, config.getClass());
		redisCache.delete(key_backup, config.getClass());
	}
	
	@Override
	public int getExpire() {
		return 0;
	}
	
}
