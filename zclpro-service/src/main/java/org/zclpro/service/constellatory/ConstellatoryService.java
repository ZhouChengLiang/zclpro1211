package org.zclpro.service.constellatory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstellatoryService {
	
	@Autowired
	private ConstellatoryCache constellatoryCache;
	
	public void pullConstellatory3(){
		constellatoryCache.pullConstellatory3();
	}
}
