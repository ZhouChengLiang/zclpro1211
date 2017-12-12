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
}
