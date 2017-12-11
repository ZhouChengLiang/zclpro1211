package org.zclpro.db.sign.entity;

import org.zclpro.db.common.IPOJOCache;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRule extends IPOJOCache{
    private Integer id;

    private Integer signPattern;

    private Integer signIndex;

    private Integer signExperience;

    private Integer signIntegral;

    private Integer countyCode;

}