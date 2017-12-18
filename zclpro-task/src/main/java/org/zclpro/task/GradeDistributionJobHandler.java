package org.zclpro.task;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zclpro.service.gradeinfo.GradeInfoService;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 定时计算等级信息,包括更新等级人数与统计各等级的分布人员
 * @author Administrator
 *
 */
@JobHander(value = "GradeDistribution")
@Component
public class GradeDistributionJobHandler extends IJobHandler{
	@Autowired
	private GradeInfoService gradeInfoService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		Instant start = Instant.now();
		gradeInfoService.generateGradeDistribution();;
		XxlJobLogger.log(">>>>>>>>>It costs {0} execute GradeDistributionJobHandler<<<<<<<<<<", Duration.between(start, Instant.now()));
		return ReturnT.SUCCESS;
	}
}