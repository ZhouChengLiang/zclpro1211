package org.zclpro.common.redistool;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisTemplate extends JedisPool {
	public RedisTemplate(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password,
			int database) {
		super(poolConfig, host, port, timeout, password, database);
	}

	public RedisTemplate(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, int db) {
		super(poolConfig, host, port, timeout, null, db);
	}

	public Jedis get() {
		return getResource();
	}

	public String hmset(String key, Map<String, String> hash) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.hmset(key, hash);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}
	@Override
	public void returnBrokenResource(Jedis connect){
		if (connect == null) {
            return;
        }
        connect.close();
	}
	
	@Override
	public void returnResource(Jedis connect){
		if (connect == null) {
            return;
        }
        connect.close();
	}
	
	public List<String> hmget(String key, String... fields) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.hmget(key, fields);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.set(key, value);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public String set(String key, String value, int seconds) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			String val = jedis.set(key, value);
			jedis.expire(key, seconds);
			return val;
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public String set(String key, String value, long unixTime) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			String val = jedis.set(key, value);
			jedis.expireAt(key, unixTime);
			return val;
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.get(key);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public String hget(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.hget(key, field);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long hset(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.hset(key, field, value);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.hgetAll(key);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long incr(String key) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.incr(key);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long incr(String key, long integer) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.incrBy(key, integer);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long incrAndExpire(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			Long v = jedis.incr(key);
			jedis.expire(key, seconds);
			return v;
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long incrAndExpire(String key, long unixTime) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			Long v = jedis.incr(key);
			jedis.expireAt(key, unixTime);
			return v;
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long incrAndExpire(String key, long integer, int seconds) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			Long v = jedis.incrBy(key, integer);
			jedis.expire(key, seconds);
			return v;
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long expire(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.expire(key, seconds);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long ttl(String key) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.ttl(key);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public String getSet(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.getSet(key, value);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Response<List<Object>> execute(Callback callback) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			Pipeline pipeline = jedis.pipelined();
			callback.execute(pipeline);
			return pipeline.exec();
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public void del(final String... keys) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.del(keys);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long hdel(String key, String... fields) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.hdel(key, fields);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.exists(key);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Double score(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.zscore(key, field);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 向sort set add数据之前清空该key
	 * 
	 * @param key
	 * @param scoreMembers
	 * @return
	 */
	public List<Object> zaddBeforeClear(String key, Map<String, Double> scoreMembers) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			Pipeline pipline = jedis.pipelined();
			pipline.del(key);
			pipline.zadd(key, scoreMembers);
			return pipline.exec().get();
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public interface Callback {
		void execute(Pipeline pipeline);
	}

	public Long zadd(String key, double score, String member) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.zadd(key, score, member);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long zcount(String key, double min, double max) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.zcount(key, min, max);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long zadd(String key, Map<String, Double> scoreMembers) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.zadd(key, scoreMembers);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Set<String> zrangeByScore(String key, double min, double max) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.zrangeByScore(key, min, max);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Set<String> zrange(String key, long start, long end) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.zrange(key, start, end);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long zrem(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.zrem(key, member);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long zremrangeByScore(String key, double min, double max) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.zremrangeByScore(key, min, max);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Set<String> keys(String pattern) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.keys(pattern);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long sadd(String key, String... members) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.sadd(key, members);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Long srem(String key, String... members) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.srem(key, members);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public List<String> scan(final String cursor) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			ScanResult<String> scanResult = jedis.scan(cursor);
			return scanResult.getResult();
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public Set<String> smembers(String key) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.smembers(key);
		} catch (JedisConnectionException e) {
			returnBrokenResource(jedis);
			throw e;
		} finally {
			returnResource(jedis);
		}
	}
}
