package org.zclpro.db.grade.entity;

import java.util.Date;

import org.zclpro.db.common.SearchDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeDistribution extends SearchDate{
    private String curdate;

    private Integer curgrade;

    private Integer curexp;

    private Integer curusers;
    
    private Date curtime;

    private Integer countyCode;

}