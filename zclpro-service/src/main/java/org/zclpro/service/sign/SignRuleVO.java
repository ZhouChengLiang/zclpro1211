package org.zclpro.service.sign;

import org.springframework.beans.BeanUtils;
import org.zclpro.db.sign.entity.SignRule;

import lombok.Data;

@Data
public class SignRuleVO {
	private Integer id;

    private Integer signIndex;

    private Integer signExperience;

    private Integer signIntegral;
    
    public static SignRuleVO convertFrom(SignRule source){
    	SignRuleVO target = new SignRuleVO();
    	BeanUtils.copyProperties(source, target);
    	return target;
    }
}
