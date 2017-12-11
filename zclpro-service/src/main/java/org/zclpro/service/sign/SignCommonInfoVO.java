package org.zclpro.service.sign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import lombok.Data;

@Data
public class SignCommonInfoVO {
	
	private String animalsYear;
	
	private String weekday;
	
	private String lunarYear;
	
	private String date;
	
	private String holiday;
	
	private String lunar;
	
	private String day;
	
	private List<String> suits;
	
	private List<String> avoids;
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	public static SignCommonInfoVO convertFrom(SignCommonInfo source){
		SignCommonInfoVO target = new SignCommonInfoVO();
		BeanUtils.copyProperties(source, target);
		target.setAvoids(getResult(Arrays.asList(source.getAvoid().split("\\."))));
		target.setSuits(getResult(Arrays.asList(source.getSuit().split("\\."))));
    	return target;
	}
	
	private static List<String> getResult(List<String> source) {
		source = source.stream().sorted((x,y)->Integer.compare(x.length(), y.length())).collect(Collectors.toList());
		List<String> result = new ArrayList<>();
		int index = 0;
		for (String str : source) {
			if (str.length() > 2) {
				index = index + 2;
			} else {
				index = index + 1;
			}
			if (index < 11) {
				result.add(str);
			}
		}
		return result;
	}
	
	/**
	 * @param source 原字符串集合
	 * @return
	 */
	public static List<String> getResultStream(List<String> source){
		Integer missPart = source.stream().sorted((x,y)->Integer.compare(x.length(), y.length())).filter((str)->str.length()<=4).limit(10).map((str)->{
			if(str.length()>2){
				return -1;
			}else{
				return 0;
			}
		}).reduce(Integer::sum).get();
		Integer leftPart = Integer.sum(missPart,10);
		Integer fullPart = (int) source.stream().sorted((x,y)->Integer.compare(x.length(), y.length())).filter((str)->str.length()<=4).count();
		Integer lastPart = (leftPart + Integer.min(fullPart, 10))/2;
		return source.stream().sorted((x,y)->Integer.compare(x.length(), y.length())).filter((str)->str.length()<=4).limit(lastPart).collect(Collectors.toList());
	}
}
