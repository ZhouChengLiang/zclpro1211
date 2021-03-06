package org.zclpro.service.sign;

import java.time.Duration;
import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.service.constellatory.ConstellatoryService;
import org.zclpro.service.enums.ConstellatoryEnum;
import org.zclpro.service.enums.FortuneConditionEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-service.xml"})
@ActiveProfiles("dev")
public class ConstellatoryServiceTest {
	
	@Autowired
	private ConstellatoryService constellatoryService;
	
	@Test
	public void test3(){
		Instant start = Instant.now();
		constellatoryService.pullConstellatory3();
		System.out.println("test3 pullConstellatory cost>>>>>>>"+Duration.between(start,  Instant.now()));
	}
	
	@Test
	public void test0(){
		for(ConstellatoryEnum ce :ConstellatoryEnum.values()){
			constellatoryService.pullConstellatory_week(FortuneConditionEnum.WEEK, ce);
		}
	}
	
	@Test
	public void test1(){
		for(ConstellatoryEnum ce :ConstellatoryEnum.values()){
//			constellatoryService.pullConstellatory_day(FortuneConditionEnum.TODAY, ce);
			constellatoryService.pullConstellatory_day(FortuneConditionEnum.TOMOROW, ce);
		}
	}
}
