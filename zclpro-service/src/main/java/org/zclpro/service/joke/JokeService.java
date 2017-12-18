package org.zclpro.service.joke;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JokeService {
	
	@Autowired
	private JokeCache jokeCache;
	
	/**
	 * 提供给定时任务拉取笑话信息
	 */
	public void pullJokeInfos(){
		jokeCache.pullJokeInfos();
	}
}
