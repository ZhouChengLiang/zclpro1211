package org.zclpro.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zclpro.service.myh.MyhTaskService;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;

/**
 * 拉取名医汇医院一级科室数据
 * @author Administrator
 *
 */
@JobHander(value = "MyhDepartmentFirst")
@Component
public class MyhDepartmentFirstJobHandler extends IJobHandler{
	
	@Autowired
	private MyhTaskService myhTaskService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		myhTaskService.grabDepartmentLevelFirstDatas();
		return ReturnT.SUCCESS;
	}

}
