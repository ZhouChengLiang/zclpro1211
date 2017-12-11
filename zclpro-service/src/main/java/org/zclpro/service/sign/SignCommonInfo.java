package org.zclpro.service.sign;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignCommonInfo {
	
	private String avoid;
	
	private String animalsYear;
	
	private String weekday;
	
	private String suit;
	
	private String lunarYear;
	
	private String date;
	
	private String holiday;
	
	private String lunar;
	
	private String day;
	
	private Integer errorCode;
	
	private String reason;
	
	public SignCommonInfo changeSelf(SignCommonInfo self){
		String date = self.getDate();
		String[] dateArr = date.split("-");
		String resultDate = dateArr[0]+"年"+dateArr[1]+"月"+dateArr[2]+"日";
		self.setDate(resultDate);
		self.setDay(dateArr[2]);
		self.setAnimalsYear(self.getAnimalsYear()+"年");
		return self;
	}
	
}
