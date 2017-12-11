package org.zclpro.db.sign.entity;

import org.zclpro.common.redistool.RedisHashIgnore;
import org.zclpro.db.common.IPOJOCache;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignPattern extends IPOJOCache{
    private Integer id;

    private Integer signType;

    private Integer signStatus;

    private Integer signLoopcycle;

    private Integer signPerioddays;

    private Integer signContinuous;

    private Integer countyCode;
    
    @RedisHashIgnore
    public transient static final Integer EFFECTIVE = 1;
    
    @RedisHashIgnore
    public transient static final Integer INEFFECTIVE = 0;
    /**
     * 签到方式  按周发送奖励
     */
    @RedisHashIgnore
    public transient static final Integer WEEKLY = 0;
    
    /**
     * 签到方式 自定义
     */
    @RedisHashIgnore
    public transient static final Integer SELFCUSTOM = 1;

}