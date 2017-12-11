package org.zclpro.db.sign.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRecord {
    private Integer userid;

    private String lastdate;

    private Integer serialdays;

    private Integer countyCode;
    
    private Integer signType;

    private Integer signDays;
}