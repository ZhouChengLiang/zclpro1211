package org.zclpro.service.sign;

import java.util.Arrays;
import java.util.List;

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
	
	@Test
	public void test2(){
		List<Integer> ids = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		ids.forEach((countyCode)->gradeInfoService.getGradeInfos(countyCode));
	}
	
	@Test
	public void test3(){
		gradeInfoService.generateGradeDistribution_last();
	}
	
}
