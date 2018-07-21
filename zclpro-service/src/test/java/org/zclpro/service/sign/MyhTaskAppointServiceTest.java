package org.zclpro.service.sign;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.service.myh.MyhAppointTaskService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-service.xml"})
@ActiveProfiles("dev")
public class MyhTaskAppointServiceTest {
	
	@Autowired
	private MyhAppointTaskService myhAppointTaskService;
	
	@Test
	public void test0(){
		try {
			myhAppointTaskService.grabMyhData("330000_7801");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
