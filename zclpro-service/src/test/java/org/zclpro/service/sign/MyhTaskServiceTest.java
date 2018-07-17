package org.zclpro.service.sign;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.service.myh.MyhTaskService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-service.xml"})
@ActiveProfiles("dev")
public class MyhTaskServiceTest {
	
	@Autowired
	private MyhTaskService myhTaskService;
	
	@Test
	public void test0(){
		myhTaskService.grabHospitalDatas();
	}
}
