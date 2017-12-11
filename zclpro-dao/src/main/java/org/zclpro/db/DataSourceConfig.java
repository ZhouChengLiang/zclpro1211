package org.zclpro.db;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class DataSourceConfig {

	@Profile(value = "test")
	@Bean(name = "jdbcConfig")
	Properties propertiesTest() throws IOException {
		Properties properties = new Properties();
		properties.load(new ClassPathResource("prop/jdbc_test.properties").getInputStream());
		return properties;
	}
	@Profile(value = "dev")
	@Bean(name = "jdbcConfig")
	Properties propertiesDev() throws IOException {
		Properties properties = new Properties();
		properties.load(new ClassPathResource("prop/jdbc.properties").getInputStream());
		return properties;
	}
	
	@Profile(value = "pre")
	@Bean(name = "jdbcConfig")
	Properties propertiesPre() throws IOException {
		Properties properties = new Properties();
		properties.load(new ClassPathResource("prop/jdbc_pre.properties").getInputStream());
		return properties;
	}
	
	@Profile(value = "prod")
	@Bean(name = "jdbcConfig")
	Properties propertiesProd() throws IOException {
		Properties properties = new Properties();
		properties.load(new ClassPathResource("prop/jdbc_production.properties").getInputStream());
		return properties;
	}

	@Bean(destroyMethod = "close")
	public DataSource dataSource(@Qualifier("jdbcConfig")Properties env) throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(env.getProperty("jdbc.driverClass"));
		dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		dataSource.setUser(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("jdbc.maxPoolSize")));
		dataSource.setMinPoolSize(Integer.parseInt(env.getProperty("jdbc.minPoolSize")));
		dataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("jdbc.maxIdleTime")));
		dataSource.setInitialPoolSize(Integer.parseInt(env.getProperty("jdbc.initialPoolSize")));
		dataSource.setAutoCommitOnClose(Boolean.parseBoolean(env.getProperty("jdbc.autoCommitOnClose")));
		dataSource.setCheckoutTimeout(Integer.parseInt(env.getProperty("jdbc.checkoutTimeout")));
		return dataSource;
	}
}
