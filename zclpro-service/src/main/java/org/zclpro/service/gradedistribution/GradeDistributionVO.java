package org.zclpro.service.gradedistribution;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class GradeDistributionVO {
	
	private String date;
	
	private Collection<Integer> list;

	public static GradeDistributionVO changeSelf(GradeDistributionVO vo) {
		String date = vo.getDate();
		LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
		int month = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();
		date = StringUtils.joinWith(".",month,day);
		vo.setDate(date);
		return vo;
	}
}
