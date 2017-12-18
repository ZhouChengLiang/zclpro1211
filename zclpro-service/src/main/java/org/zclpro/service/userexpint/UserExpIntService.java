package org.zclpro.service.userexpint;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zclpro.db.userexpint.entity.UserExpInt;
import org.zclpro.db.userexpint.impl.UserExpIntMapper;

@Service
public class UserExpIntService {
	
	@Autowired
	private UserExpIntMapper userExpIntMapper;
	
	/**
	 * 
	 * 提供给定时任务产生用户积分经验数据记录
	 */
	public void generateUserExpInts(){
		List<UserExpInt> list = new ArrayList<>();
		for(int i = 0;i<100;i++){
			UserExpInt ueit = new UserExpInt();
			ueit.setUserId(RandomUtils.nextInt(1, 300));
			ueit.setIntegral(RandomUtils.nextInt(1, 9999));
			ueit.setExperience(RandomUtils.nextInt(1, 9999));
			ueit.setCountyCode(RandomUtils.nextInt(1, 11));
			list.add(ueit);
		}
		userExpIntMapper.batchSaveOrUpdate(list);
	}
}
