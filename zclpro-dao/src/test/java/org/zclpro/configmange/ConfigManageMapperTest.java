package org.zclpro.configmange;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.db.configmanage.impl.ConfigManageMapper;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml"})
@ActiveProfiles("dev")
public class ConfigManageMapperTest {
	@Autowired
	private ConfigManageMapper configManageMapper;
	
	@Test
	public void test0(){
		Integer id = 1;
		System.out.println(JSON.toJSON(configManageMapper.selectByPrimaryKey(id)));
	}
}
