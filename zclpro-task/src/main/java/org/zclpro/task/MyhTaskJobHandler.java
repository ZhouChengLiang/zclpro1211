package org.zclpro.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zclpro.service.myh.MyhTaskService;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;

/**
 * 拉取名医汇数据
 * @author Administrator
 *
 */
@JobHander(value = "MyhTask")
@Component
public class MyhTaskJobHandler extends IJobHandler{
	
	@Autowired
	private MyhTaskService myhTaskService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		
		return ReturnT.SUCCESS;
	}

}
