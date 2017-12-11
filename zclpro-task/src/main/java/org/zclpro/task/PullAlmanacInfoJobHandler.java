package org.zclpro.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zclpro.service.sign.SignService;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;

/**
 * 定时 拉取聚合数据的老皇历信息
 * @author Administrator
 *
 */
@JobHander(value = "PullAlmanacInfo")
@Component
public class PullAlmanacInfoJobHandler extends IJobHandler{
	@Autowired
	private SignService signService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		signService.pullAlmanacInfo();
		return ReturnT.SUCCESS;
	}

}