package org.zclpro.service.gradeinfo;

import org.springframework.beans.BeanUtils;
import org.zclpro.db.grade.entity.GradeInfo;

import lombok.Data;

@Data
public class GradeInfoVO {
	private Integer id;

    private Integer grade;

    private Integer requiredExperience;

    private String gradeName;

    private String gradeIcon;

    private Integer currentUsers;
    
    public static GradeInfoVO convertFromAtWeb(GradeInfo source){
    	GradeInfoVO target = new GradeInfoVO();
    	BeanUtils.copyProperties(source, target);
		return target;
	}
}
