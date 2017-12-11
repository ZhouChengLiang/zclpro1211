package org.zclpro.tool;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.common.redistool.MyJedisCommands;
import org.zclpro.common.tool.CommonProplConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonProplConfig.class)
@ActiveProfiles("dev")
public class CommonPropConfigTest {
	
	@Autowired
    private MyJedisCommands myJedis;
	
	@Test
    public void test0() {
		String pattern = "*";
		myJedis.delKeysWithPattern(pattern);
    }

    @Test
    public void test1() {
        
    }
}
