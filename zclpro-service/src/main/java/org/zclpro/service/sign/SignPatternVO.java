package org.zclpro.service.sign;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.zclpro.db.sign.entity.SignPattern;

import lombok.Data;

@Data
public class SignPatternVO {
	private Integer id;

    private Integer signType;

    private Integer signLoopcycle;

    private Integer signPerioddays;

    private Integer signContinuous;
    
    public static SignPatternVO convertFrom(SignPattern source){
    	SignPatternVO target = new SignPatternVO();
    	if(Objects.equals(SignPattern.WEEKLY, source.getSignType())){
    		target.setId(source.getId());
    		target.setSignType(source.getSignType());
    	}else if(Objects.equals(SignPattern.SELFCUSTOM, source.getSignType())){
    		BeanUtils.copyProperties(source, target);
    	}
    	return target;
    }
}
