package org.zclpro.gradeabout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.db.grade.impl.GradeDistributionMapper;
import org.zclpro.db.grade.impl.GradeInfoMapper;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml"})
@ActiveProfiles("dev")
public class GradeInfoAboutMapperTest {
	
	@Autowired
	private GradeInfoMapper gradeInfoMapper;
	
	@Autowired
	private GradeDistributionMapper gradeDistributionMapper;
	
	@Test
	public void test0(){
		Integer id = 1;
		System.out.println(JSON.toJSON(gradeInfoMapper.selectByPrimaryKey(id)));
	}
}
