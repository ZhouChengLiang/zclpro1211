package org.zclpro.service.sign;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.db.configmanage.entity.ConfigManage;
import org.zclpro.service.configmanage.ConfigManageService;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-service.xml"})
@ActiveProfiles("dev")
public class SignServiceTest {
	@Autowired
	private SignService signService;
	
	@Autowired
	private ConfigManageService configManageService;
	
	@Autowired
	private SignCache signCache;
	@Test
	public void test0(){
		Integer countyCode = 10;
		Map<String, Object> result = new HashMap<>();
		result = signService.getSignConfigInfo(countyCode);
		System.out.println(JSON.toJSON(result));
	}
	
	@Test
	public void test1(){
		signService.pullAlmanacInfo();
	}
	
	@Test
	public void test2(){
		LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
		for(int i = 0;i<100;i++){
			LocalDate localDateNext = localDate.plusDays(i);
			System.out.println("THE LOCALDATE>>>>>>>>>>>>>>>>>>>>"+localDateNext);
		}
	}
	
	@Test
	public void test3(){
		Instant start = Instant.now();
		LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
		for(int i = 0;i<10;i++){
			Map<String,Object> result = new HashMap<>();
			LocalDate localDateNext = localDate.plusDays(i);
			Date date = Date.from(localDateNext.atStartOfDay(ZoneId.systemDefault()).toInstant());
			String specifyDate = DateFormatUtils.format(date, "yyyy-M-d");
			SignCommonInfoVO signCommonInfoVO = signCache.getSignCommonInfo(specifyDate, result);
			System.out.println(JSON.toJSON(signCommonInfoVO));
		}
		System.out.println(Duration.between(start, Instant.now()));
	}
	
	@Test
	public void test4(){
		Integer userId = 1244;
		Integer countyCode = 10;
		System.out.println(signService.doSignIn(userId, countyCode));
	}
	
	@Test
	public void test5(){
		ConfigManage config = new ConfigManage();
		config.setId(20);
		configManageService.toggle(config);
	}
}
