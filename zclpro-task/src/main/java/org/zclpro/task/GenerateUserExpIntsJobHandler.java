package org.zclpro.task;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zclpro.service.userexpint.UserExpIntService;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 定时 产生用户经验积分数据
 * @author Administrator
 *
 */
@JobHander(value = "GenerateUserExpInts")
@Component
public class GenerateUserExpIntsJobHandler extends IJobHandler{
	@Autowired
	private UserExpIntService userExpIntService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		Instant start = Instant.now();
		userExpIntService.generateUserExpInts();
		XxlJobLogger.log(">>>>>>>>>It costs {0} execute GenerateUserExpIntsJobHandler<<<<<<<<<<", Duration.between(start, Instant.now()));
		return ReturnT.SUCCESS;
	}
}