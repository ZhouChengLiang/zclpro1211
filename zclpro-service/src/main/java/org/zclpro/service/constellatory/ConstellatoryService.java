package org.zclpro.service.constellatory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstellatoryService {
	
	@Autowired
	private ConstellatoryCache constellatoryCache;
	
	public void pullConstellatory(){
		constellatoryCache.pullConstellatory();
	}
	
	public void pullConstellatory1(){
		constellatoryCache.pullConstellatory1();
	}
	
	public void pullConstellatory2(){
		constellatoryCache.pullConstellatory2();
	}
	
	public void pullConstellatory3(){
		constellatoryCache.pullConstellatory3();
	}
}
