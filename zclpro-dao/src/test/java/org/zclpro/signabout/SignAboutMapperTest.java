package org.zclpro.signabout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.db.sign.impl.SignPatternMapper;
import org.zclpro.db.sign.impl.SignRecordMapper;
import org.zclpro.db.sign.impl.SignRuleMapper;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml"})
@ActiveProfiles("dev")
public class SignAboutMapperTest {
	
	@Autowired
	private SignRecordMapper signRecordMapper;
	
	@Autowired
	private SignPatternMapper signPatternMapper;
	
	@Autowired
	private SignRuleMapper	signRuleMapper;
	
	@Test
	public void test0(){
		Integer id = 1;
		System.out.println(JSON.toJSON(signPatternMapper.selectByPrimaryKey(id)));
		System.out.println(JSON.toJSON(signRuleMapper.selectByPrimaryKey(id)));
	}
}
