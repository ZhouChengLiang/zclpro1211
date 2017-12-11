package org.zclpro.db.grade.entity;

import lombok.Data;

@Data
public class GradeInfoKeySortNum {
	private Integer id;
	private Integer requiredExperience;
	
	public GradeInfoKeySortNum(Integer id,Integer requiredExperience){
		this.id = id;
		this.requiredExperience = requiredExperience;
	}
	
	public GradeInfoKeySortNum(){
		
	}
}
