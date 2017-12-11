package org.zclpro.db.common;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDate {
    private Integer selectDay;
    private Date startTime;
    private Date endTime;
}