package org.zclpro.service.constellatory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zclpro.service.enums.ConstellatoryEnum;
import org.zclpro.service.enums.FortuneConditionEnum;

@Service
public class ConstellatoryService {
	
	@Autowired
	private ConstellatoryCache constellatoryCache;
	
	public void pullConstellatory3(){
		constellatoryCache.pullConstellatory3();
	}
	
	/**
	 * 供单独测试拉取星座week的方法使用
	 */
	public void pullConstellatory_week(FortuneConditionEnum fortuneConditionEnum, ConstellatoryEnum constellatoryEnum) {
		constellatoryCache.pullConstellatory_week(fortuneConditionEnum, constellatoryEnum);
	}
}
