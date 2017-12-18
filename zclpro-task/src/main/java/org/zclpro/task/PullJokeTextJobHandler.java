package org.zclpro.task;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zclpro.service.joke.JokeService;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 定时 拉取聚合数据的笑话大全信息
 * @author Administrator
 *
 */
@JobHander(value = "PullJokeText")
@Component
public class PullJokeTextJobHandler extends IJobHandler{
	@Autowired
	private JokeService jokeService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		Instant start = Instant.now();
		jokeService.pullJokeInfos();
		XxlJobLogger.log(">>>>>>>>>It costs {0} execute PullJokeTextJobHandler<<<<<<<<<<", Duration.between(start, Instant.now()));
		return ReturnT.SUCCESS;
	}
}