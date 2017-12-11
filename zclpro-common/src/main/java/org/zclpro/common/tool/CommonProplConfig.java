package org.zclpro.common.tool;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.zclpro.common.redistool.MyJedisCommands;
import org.zclpro.common.redistool.RedisConnectionFactory;

import com.google.common.base.Charsets;

@Configuration
public class CommonProplConfig {

	@Bean(name = "commonProperties")
	@Profile("dev")
	Properties propertiesDev() throws IOException {
		Properties properties = new Properties();
        properties.load(new InputStreamReader(new ClassPathResource("prop/common_dev.properties").getInputStream(), Charsets.UTF_8));
        return properties;
	}
	@Profile("test")
	@Bean(name = "commonProperties")
	Properties propertiesTest() throws IOException {
		Properties properties = new Properties();
        properties.load(new InputStreamReader(new ClassPathResource("prop/common_test.properties").getInputStream(), Charsets.UTF_8));
        return properties;
	}
	@Profile("pre")
	@Bean(name = "commonProperties")
	Properties propertiesPre() throws IOException {
		Properties properties = new Properties();
        properties.load(new InputStreamReader(new ClassPathResource("prop/common_pre.properties").getInputStream(), Charsets.UTF_8));
        return properties;
	}
	@Profile("prod")
	@Bean(name = "commonProperties")
	Properties properties() throws IOException {
		Properties properties = new Properties();
        properties.load(new InputStreamReader(new ClassPathResource("prop/common_prod.properties").getInputStream(), Charsets.UTF_8));
        return properties;
	}


	/**
	 * reids连接池管理
	 * @return
	 */
	@Bean(initMethod = "init")
	public RedisConnectionFactory redisConnectionManager(@Qualifier("commonProperties")Properties env) {
		RedisConnectionFactory manager = new RedisConnectionFactory();
		manager.setCluster(Boolean.parseBoolean(env.getProperty("redis.isCluster")));
		manager.setHost(env.getProperty("redis.host"));
		manager.setPort(Integer.parseInt(env.getProperty("redis.port")));
		manager.setMaxIdle(Integer.parseInt(env.getProperty("redis.maxIdle")));
		manager.setMaxTotal(Integer.parseInt(env.getProperty("redis.maxTotal")));
		manager.setMaxWaitMillis(Long.parseLong(env.getProperty("redis.maxWaitMillis")));
		manager.setPassword(env.getProperty("redis.password"));
		manager.setConnectionTimeout(Integer.parseInt(env.getProperty("redis.connectionTimeout")));
		manager.setSoTimeout(Integer.parseInt(env.getProperty("redis.soTimeout")));
		manager.setMaxAttempts(Integer.parseInt(env.getProperty("redis.maxAttempts")));
		return manager;
	}
	
	@Bean
	public MyJedisCommands redisCacheManager(RedisConnectionFactory redisConnectionManager) {
		return redisConnectionManager.createRedisCacheManager();
	}
	
	/**
	 * spring中rest请求客户端工具
	 * @return
	 */
	@Bean
	public RestTemplate restTemplate() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		return new RestTemplate(requestFactory);
	}
	
}