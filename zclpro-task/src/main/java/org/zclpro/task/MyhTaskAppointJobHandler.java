package org.zclpro.task;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zclpro.service.myh.MyhAppointTaskService;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 拉取单独医院的名医汇数据
 * @author Administrator
 *
 */
@JobHander(value = "MyhTaskAppoint")
@Component
public class MyhTaskAppointJobHandler extends IJobHandler{
	
	@Autowired
	private MyhAppointTaskService myhAppointTaskService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("MyhTaskAppointJobHandler >>> "+Arrays.asList(params));
		myhAppointTaskService.grabMyhData(params);
		return ReturnT.SUCCESS;
	}

}
