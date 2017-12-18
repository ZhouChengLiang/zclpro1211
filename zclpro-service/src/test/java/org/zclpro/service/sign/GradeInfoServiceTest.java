package org.zclpro.service.sign;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.service.gradeinfo.GradeInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-service.xml"})
@ActiveProfiles("dev")
public class GradeInfoServiceTest {
	
	@Autowired
	private GradeInfoService gradeInfoService;
	
	@Test
	public void test0(){
		gradeInfoService.calculateGradeInfo();
	}
	
	@Test
	public void test1(){
		gradeInfoService.generateGradeDistribution();
	}
}
