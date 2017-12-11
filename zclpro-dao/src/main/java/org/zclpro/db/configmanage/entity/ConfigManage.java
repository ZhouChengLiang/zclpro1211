package org.zclpro.db.configmanage.entity;

import java.util.Objects;

import org.zclpro.common.redistool.RedisHashIgnore;
import org.zclpro.db.common.IPOJOCache;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigManage extends IPOJOCache{
    private Integer id;

    private Integer configType;

    private Integer configStatus;

    private Integer countyCode;
    
    /**
     * 标识 “用户成长等级” 配置项
     */
    @RedisHashIgnore
    public transient static final int GRADE = 0;
    
    /**
     * 标识“签到”配置项
     */
    @RedisHashIgnore
    public transient static final int SIGNIN = 1;
    
    @RedisHashIgnore
    public transient static final int OPENED = 1;
    
    @RedisHashIgnore
    public transient static final int CLOSED = 0;
    
    public static enum ConfigNameEnum{
    	GRADE("成长等级",0),
    	SIGN("签到",1);
    	@Getter
    	private String name;
    	@Getter
    	private Integer type;
    	ConfigNameEnum(String name,Integer type){
            this.name=name;
            this.type=type;
        }
    	 public static ConfigNameEnum getByType(Integer type) {
			for (ConfigNameEnum configNameEnum : ConfigNameEnum.values()) {
				if (Objects.equals(configNameEnum.getType(), type)) {
					return configNameEnum;
				}
			}
			return null;
		}
    }
    
}