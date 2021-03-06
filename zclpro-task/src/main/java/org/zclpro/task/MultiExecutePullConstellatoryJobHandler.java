package org.zclpro.task;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zclpro.service.constellatory.ConstellatoryService;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 定时拉取星座运势信息
 * @author Administrator
 *
 */
@JobHander(value = "MultiExecutePullConstellatory")
@Component
public class MultiExecutePullConstellatoryJobHandler extends IJobHandler{
	
	@Autowired
	private ConstellatoryService constellatoryService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		Instant start = Instant.now();
		constellatoryService.pullConstellatory3();
		XxlJobLogger.log("MultiExecutePullConstellatory Cost>>>>>"+Duration.between(start, Instant.now()));
		return ReturnT.SUCCESS;
	}

}
