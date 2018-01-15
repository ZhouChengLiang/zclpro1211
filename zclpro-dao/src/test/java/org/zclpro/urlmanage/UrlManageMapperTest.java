package org.zclpro.urlmanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.db.urlmanage.entity.UrlManage;
import org.zclpro.db.urlmanage.impl.UrlManageMapper;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml"})
@ActiveProfiles("dev")
public class UrlManageMapperTest {
	
	@Autowired
	private UrlManageMapper	urlManageMapper;
	
	@Test
	public void test0(){
		Map<String,Object> map = new HashMap<>();
		Integer countyCode = 8;
		List<UrlManage> list = urlManageMapper.selectByCountyCode(countyCode);
		System.out.println(JSON.toJSONString(list));
		list.forEach((obj)-> map.put(obj.getUrlName(),StringUtils.trim(obj.getUrlAddr())));
		System.out.println(JSON.toJSONString(map));
	}
}
