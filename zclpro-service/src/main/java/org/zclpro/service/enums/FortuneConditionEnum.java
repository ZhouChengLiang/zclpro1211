package org.zclpro.service.enums;

import lombok.Getter;

public enum FortuneConditionEnum {
	TODAY("today"),
	TOMOROW("tomorrow"),
	WEEK("week"),
	NEXTWEEK("nextweek"),
	MONTH("month"),
	YEAR("year");
	@Getter
	private String name;
	private FortuneConditionEnum(String name){
		this.name = name;
	}
}
