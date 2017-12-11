package org.zclpro.common.redistool;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Setter;
import lombok.ToString;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
@Setter
@ToString
public class RedisConnectionFactory {

	private Logger log = LoggerFactory.getLogger(RedisConnectionFactory.class);

	private JedisCluster jedisCluster;
	
	private static JedisPool pool = null;
	
	private boolean isCluster;
	
	private int maxTotal;
	
	private int maxIdle;
	
	private long maxWaitMillis;
	
	private String host;
	
	private int port;
	
	private int connectionTimeout = 0;
	
	private int soTimeout = 0;
	
	private int maxAttempts = 3;
	
	private String password;
	public void init() {
		if(isCluster){
			HostAndPort hostPort = new HostAndPort(host, port);
			JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
			jedisPoolConfig.setMaxIdle(maxIdle);
			jedisPoolConfig.setMaxTotal(maxTotal);
			jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
			jedisCluster = new JedisCluster(hostPort, connectionTimeout, soTimeout, maxAttempts, password, jedisPoolConfig);

		}else{
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(maxIdle);
			config.setMaxWaitMillis(maxWaitMillis);
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, host, port, connectionTimeout, password);
		}
		
	}
	
	public Object getConnect() {

		Object obj = null;
		if(isCluster){
			obj = jedisCluster;
		}else{
			obj = pool.getResource();
		}
		return obj;
	}
	
	public void returnResource(Jedis jedisConn){
		if(jedisConn == null){
			return;
		}
		jedisConn.close();
	}
	
	public void returnBrokenResource(Jedis jedisConn){
		if(jedisConn == null){
			return;
		}
		jedisConn.close();
	}
	
	public MyJedisCommands createRedisCacheManager(){
		if(isCluster){
			return new MyJRedisCluster(this);
		}else{
			return new MyJedis(this);
		}
	}
}
