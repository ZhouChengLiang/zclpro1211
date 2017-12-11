package org.zclpro.service;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.zclpro.common.tool.CommonProplConfig;

@Configuration
@ComponentScan(basePackages="org.zclpro.service")
@Import(value = CommonProplConfig.class)
public class ServiceConfig {
	@Profile(value = {"dev"})
	@Bean(name = "configProperties")
	Properties propertiesDev() throws IOException {
		Properties properties = new Properties();
		properties.load(new ClassPathResource("config_dev.properties").getInputStream());
		return properties;
	}

	@Bean(name = "configProperties")
	@Profile(value = {"test"})
	Properties propertiesTest() throws IOException {
		Properties properties = new Properties();
		properties.load(new ClassPathResource("config_test.properties").getInputStream());
		return properties;
	}
	
	@Bean(name = "configProperties")
	@Profile(value = {"pre"})
	Properties propertiesPre() throws IOException {
		Properties properties = new Properties();
		properties.load(new ClassPathResource("config_pre.properties").getInputStream());
		return properties;
	}
	
	@Bean(name = "configProperties")
	@Profile(value = {"prod"})
	Properties propertiesProd() throws IOException {
		Properties properties = new Properties();
		properties.load(new ClassPathResource("config_prod.properties").getInputStream());
		return properties;
	}
}
