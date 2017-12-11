package org.zclpro.common.redistool;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.zclpro.common.reflect.ReflectionUtils;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

@Slf4j
public class MyJRedisCluster implements MyJedisCommands {

    MyJRedisCluster(RedisConnectionFactory redisConnectionManager) {
        this.redisConnectionManager = redisConnectionManager;
    }

    private RedisConnectionFactory redisConnectionManager;

    private JedisCluster getConnect() {
        return (JedisCluster) redisConnectionManager.getConnect();
    }


    /**
     * get
     *
     * @param key
     * @return
     */
    @Override
    public byte[] get(byte[] key) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            log.error("get fail,", e);
            throw e;
        }
    }

    /**
     * <p>
     * 将一个或多个成员元素及其分数值加入到有序集当中
     * <p>
     * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。分数值可以是整数值或双精度浮点数
     * <p>
     * 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作
     * <p>
     * 集合中只保存30天内数据
     *
     * @param key
     * @param score
     * @param member
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员
     */
    @Override
    public Long zadd(String key, double score, String member) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zadd(key, score, member);
        } catch (Exception e) {
            log.error("zadd fail,", e);
            throw e;
        }
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            log.error("sadd fail,", e);
            throw e;
        }
    }

    /**
     * 仅向有序集合中添加元素，不会进行集合截取操作
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    @Override
    public Long zaddWithOutRem(String key, double score, String member) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zadd(key, score, member);
        } catch (Exception e) {
            log.error("zaddWithOutRem fail,", e);

            throw e;
        }
    }

    @Override
    public Set<String> zrangeByScore(final String key, final long min, final long max) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("zrangeByScore fail,", e);

            throw e;
        }
    }

    @Override
    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("zrangeByScore fail,", e);

            throw e;
        }
    }

    /**
     * Redis Zrange 返回有序集中，指定区间内的成员。
     * <p>
     * 其中成员的位置按分数值递增(从小到大)来排序 具有相同分数值的成员按字典序来排列
     * <p>
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public Set<String> zrange(final String key, final long start, final long end) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            log.error("zrange fail,", e);

            throw e;
        }
    }

    /**
     * <p>
     * 返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序
     * <p>
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
     *
     * @param key
     * @param start
     * @param end
     * @return 有序集 key 的成员
     */
    @Override
    public Set<String> zrevrange(final String key, final long start, final long end) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            log.error("zrevrange fail,", e);

            throw e;
        }
    }

    /**
     * 返回有序集合中指定分数区间内的成员，分数由低到高排序
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset,
                                              final int count) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrangeByScoreWithScores fail,", e);

            throw e;
        }
    }

    /**
     * 返回有序集合中指定分数区间内的成员，分数由高到低排序
     *
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param count
     * @return
     */
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset,
                                                 final int count) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } catch (Exception e) {
            log.error("zrevrangeByScoreWithScores fail,", e);

            throw e;
        }
    }

    /**
     * 返回有序集合中指定分数区间内的成员，分数由高到低排序
     *
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param count
     * @return
     */
    @Override
    public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset,
                                        final int count) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            log.error("zrevrangeByScore fail,", e);

            throw e;
        }
    }

    /**
     * 返回有序集合中指定分数区间内的成员，分数由高到低排序
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            log.error("zrevrangeByScoreWithScores fail,", e);

            throw e;
        }
    }

    /**
     * <p>
     * 移除有序集中的一个或多个成员,不存在的成员将被忽略
     *
     * @param key
     * @param members
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    @Override
    public long zRemove(final String key, final String... members) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zrem(key, members);
        } catch (Exception e) {
            log.error("zRemove fail,", e);

            throw e;
        }
    }

    /**
     * get
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        if (key == null) {
            return null;
        }
        JedisCluster jedis = getConnect();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            log.error("get fail,", e);

            throw e;
        }
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String set(byte[] key, byte[] value) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.set(key, value);

        } catch (Exception e) {
            log.error("set fail,", e);

            throw e;
        }
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String set(String key, String value) {
        return set(key, value, 60);
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param expire
     * @return Status code reply
     */
    @Override
    public String set(byte[] key, byte[] value, int expire) {
        JedisCluster jedis = getConnect();
        try {
            String result = jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
            return result;
        } catch (Exception e) {
            log.error("set fail,", e);
            throw e;
        }
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param expire
     * @return Status code reply
     */
    @Override
    public String set(String key, String value, int expire) {
        JedisCluster jedis = getConnect();
        try {
            if (expire != 0) {
                return jedis.setex(key, expire, value);
            } else {
                return jedis.set(key, value);
            }
        } catch (Exception e) {
            log.error("set fail,", e);

            throw e;
        }
    }

    /**
     * <p>
     * 将整个对象以(字段-值)形式保存到哈希表中
     * <p>
     * 此命令会覆盖哈希表中已存在的字段
     * <p>
     * 如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作
     *
     * @param key
     * @param value
     * @param expire
     * @return 如果命令执行成功，返回 OK
     */
    @Override
    public String hmset(String key, Object value, int expire) {
        JedisCluster jedis = getConnect();
        try {
            String ret = jedis.hmset(value.getClass().getName() + SPLITCHAR + key, Pojo2Map.toMapWithoutIgnore(value));
            if (expire != 0) {
                jedis.expire(value.getClass().getName() + SPLITCHAR + key, expire);

            }
            return ret;
        } catch (Exception e) {
            log.error("hmset fail,", e);
            throw e;
        }
    }

    /**
     * hmset 将对象设置成map，设置到redis hash表中
     *
     * @param key
     * @param value
     * @return Status code reply
     */
    @Override
    public String hmset(String key, Object value) {
        if (value == null) {
            return "";
        }
        return hmset(key, value, 0);
    }

    /**
     * hmset 将对象设置成map，设置到redis hash表中
     *
     * @param key
     * @return Status code reply
     */
    @Override
    public <T> T hmget(String key, Class<T> clazz) {
        JedisCluster jedis = getConnect();
        try {
            if (jedis.exists(clazz.getName() + SPLITCHAR + key)) {
                Map<String, String> hm = jedis.hgetAll(clazz.getName() + SPLITCHAR + key);
                return Pojo2Map.convert2Object(hm, clazz);
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error("hmget fail,", e);

            throw e;
        }
    }

    /**
     * 命令用于返回哈希表中指定字段的值
     *
     * @param key
     * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回null
     */
    @Override
    public String hget(String key, Class<?> clazz, String fieldName) {
        JedisCluster jedis = getConnect();
        try {
            String result = jedis.hget(clazz.getName() + SPLITCHAR + key, fieldName);
            if (result != null && result.equals("nil")) {
                return null;
            }
            return result;
        } catch (Exception e) {
            log.error("hget fail,", e);

            throw e;
        }
    }

    /**
     * <p>
     * 用于为哈希表中的字段赋值
     * <p>
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
     * <p>
     * 如果字段已经存在于哈希表中，旧值将被覆盖
     *
     * @param key
     * @param field
     * @param value
     * @return Long 如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0
     */
    @Override
    public Long hset(final String key, final String field, final String value) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            log.error("hset fail,", e);

            throw e;
        }
    }

    /**
     * 设置对象obj中的field字段
     *
     * @param key
     * @param obj
     * @param field
     * @return
     */
    @Override
    public Long hset(final String key, Object obj, final String field) {

        JedisCluster jedis = getConnect();
        try {
            Field f = ReflectionUtils.getDeclaredField(obj, field);
            f.setAccessible(true);
            Object value = null;
            if (f.getType().equals(java.util.Date.class)) {
                value = ((java.util.Date) f.get(obj)).getTime();
            } else {
                value = f.get(obj);
            }
            Long ret = jedis.hset(obj.getClass().getName() + SPLITCHAR + key, field, value.toString());
            f.setAccessible(false);
            return ret;
        } catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
            log.error("hset fail,", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("hset fail,", e);

            throw e;
        }
    }

    /**
     * 修改hash存储中的字段值
     *
     * @param key
     * @return
     */
    @Override
    public Long hsetObjectFieldValue(final String key, Class<?> clazz, final String fieldName, final Object value) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.hset(clazz.getName() + ":" + key, fieldName, value.toString());
        } catch (Exception e) {
            log.error("hsetObjectFieldValue fail,", e);

            throw e;
        }
    }

    /**
     * <p>
     * 用于将一个或多个值插入到列表的尾部(最右边)。
     * <p>
     * 如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。 当列表存在但不是列表类型时，返回一个错误。
     *
     * @param key
     * @param strings
     * @return 执行 RPUSH 操作后，列表的长度
     */
    @Override
    public Long listRpush(final String key, final String... strings) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.rpush(key, strings);
        } catch (Exception e) {
            log.error("listRpush fail,", e);

            throw e;
        }
    }

    /**
     * <p>
     * 用于将一个或多个值插入到列表的尾部(左边)。
     * <p>
     * 如果列表不存在，一个空列表会被创建并执行 此 操作。 当列表存在但不是列表类型时，返回一个错误。
     *
     * @param key
     * @param strings
     * @return 执行 操作后，列表的长度
     */
    @Override
    public Long listLpush(final String key, final String... strings) {
        return listLpushAndFixedLength(key, -1, strings);
    }

    /**
     * <p>
     * 用于将一个或多个值插入到列表的尾部(左边)。
     * <p>
     * 如果列表不存在，一个空列表会被创建并执行 此 操作。 当列表存在但不是列表类型时，返回一个错误。
     *
     * @param key
     * @param strings
     * @param remain  list集合保留的长度 (保留最新值)
     * @return 执行 操作后，列表的长度
     */
    @Override
    public Long listLpushAndFixedLength(final String key, int remain, final String... strings) {
        JedisCluster jedis = getConnect();
        try {
            if (remain == -1) {
                return jedis.lpush(key, strings);

            } else {
                long result = jedis.lpush(key, strings);
                jedis.ltrim(key, 0, remain - 1);
                return result;
            }
        } catch (Exception e) {
            log.error("listLpushAndFixedLength fail,", e);

            throw e;
        }
    }

    /**
     * <p>
     * 用于将一个或多个值插入到列表的尾部(右边边)，从左截取remain个。
     * <p>
     * 如果列表不存在，一个空列表会被创建并执行 此 操作。 当列表存在但不是列表类型时，返回一个错误。
     *
     * @param key
     * @param strings
     * @param remain  list集合保留的长度 (保留最新值)
     * @return 执行 操作后，列表的长度
     */
    @Override
    public Long listRpushAndFixedLength(final String key, int remain, final String... strings) {
        JedisCluster jedis = getConnect();
        try {
            if (remain == -1) {
                return jedis.rpush(key, strings);

            } else {
                long result = jedis.rpush(key, strings);
                jedis.ltrim(key, 0, remain - 1);
                return result;
            }
        } catch (Exception e) {
            log.error("listLpushAndFixedLength fail,", e);

            throw e;
        }
    }

    /**
     * <p>
     * 移除列表中与参数 record 相等的元素
     *
     * @param key
     * @param record
     * @return 被移除元素的数量。 列表不存在时返回 0 。
     */
    @Override
    public Long listremove(final String key, final String record) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.lrem(key, 0, record);
        } catch (Exception e) {
            log.error("listremove fail,", e);

            throw e;
        }
    }

    /**
     * <p>
     * 返回列表中指定区间内的元素
     * <p>
     * 区间以偏移量 START 和 END 指定, 其中 0 表示列表的第一个元素，以此类推
     * <p>
     * 负数下标，以 -1 表示列表的最后一个元素，以此类推
     *
     * @param key
     * @param start
     * @param end
     * @return 一个列表，包含指定区间内的元素
     */
    @Override
    public List<String> listLrange(final String key, final long start, final long end) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            log.error("listLrange fail,", e);

            throw e;
        }
    }

    /**
     * delele
     *
     * @param key
     */
    @Override
    public Long delete(byte[] key) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.del(key);
        } catch (Exception e) {
            log.error("delete fail,", e);

            throw e;
        }
    }

    /**
     * 根据key删除记录
     *
     * @param key
     */
    @Override
    public Long delete(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return delete(key.getBytes(Charsets.UTF_8));
    }

    @Override
    public void expire(String key, int expire) {
        JedisCluster jedis = getConnect();
        try {
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } catch (Exception e) {
            log.error("expire fail,", e);

            throw e;
        }

    }

    /**
     * 删除hash存储的对象
     *
     * @param key
     * @param clazz 对象的运行时类型
     */
    @Override
    public Long delete(String key, Class<?> clazz) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.del(clazz.getName() + SPLITCHAR + key);
        } catch (Exception e) {
            log.error("delete fail,", e);

            throw e;
        }

    }

    @Override
    public Long ttl(String key) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.ttl(key);
        } catch (Exception e) {
            log.error("ttl fail,", e);

            throw e;
        }
    }

    /**
     * 返回一个集合中某个元素的分数，如果元素不存在或者集合不存在，则返回null
     *
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zscore(key, member);
        } catch (Exception e) {
            log.error("zscore fail,", e);

            throw e;
        }
    }

    /**
     * 判断member是否是set的一个元素，如果是返回true，否则返回false
     *
     * @param key
     * @param member
     * @return
     */
    @Override
    public Boolean sismember(String key, String member) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.sismember(key, member);
        } catch (Exception e) {
            log.error("sismember fail,", e);

            throw e;
        }
    }

    /**
     * 向集合中添加元素（或数组）
     *
     * @param key
     * @param members
     * @return
     */
    @Override
    public Long sadd(String key, String... members) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.sadd(key, members);
        } catch (Exception e) {
            log.error("sadd fail,", e);

            throw e;
        }
    }


    /**
     * 从集合中移除记录
     *
     * @param key
     * @param members
     * @return
     */
    @Override
    public Long srem(String key, String... members) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.srem(key, members);
        } catch (Exception e) {
            log.error("srem fail,", e);

            throw e;
        }
    }

    /**
     * 返回集合中的元素
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> smembers(String key) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.smembers(key);
        } catch (Exception e) {
            log.error("smembers fail,", e);

            throw e;
        }
    }

    /**
     * 判断是否存在key
     *
     * @param key
     * @return
     */
    @Override
    public boolean exsists(String key) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            log.error("exsists fail,", e);

            throw e;
        }
    }

    /**
     * 判断是否存在key
     *
     * @param key
     * @return
     */
    @Override
    public boolean exsists(byte[] key) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            log.error("exsists fail,", e);

            throw e;
        }
    }


    @Override
    public boolean exsists(Integer id, Class<?> class1) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.exists(class1.getName() + SPLITCHAR + id.toString());
        } catch (Exception e) {
            log.error("exsists fail,", e);

            throw e;
        }
    }

    /**
     * 返回无序集合的元素个数
     *
     * @param key
     * @return
     */
    @Override
    public Long scard(String key) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.scard(key);
        } catch (Exception e) {
            log.error("scard fail,", e);

            throw e;
        }
    }

    /**
     * 返回有序集合的元素个数
     *
     * @param key
     * @return
     */
    @Override
    public Long zcard(String key) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zcard(key);
        } catch (Exception e) {
            log.error("scard fail,", e);

            throw e;
        }
    }

    /**
     * 返回有序集合的在min和max分数之间的元素个数
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    @Override
    public Long zcount(String key, Long min, long max) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zcount(key, min, max);
        } catch (Exception e) {
            log.error("zcount fail,", e);

            throw e;
        }
    }

    @Override
    public void incr(String key) {
        JedisCluster jedis = getConnect();
        try {
            jedis.incr(key);
        } catch (Exception e) {
            log.error("incr fail,", e);

            throw e;
        }
    }

    @Override
    public Long hincrBy(String key, String field, int value) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.hincrBy(key, field, value);
        } catch (Exception e) {
            log.error("hincrBy fail,", e);

            throw e;
        }
    }

    @Override
    public void hincrBy(Integer id, Class<?> class1, String field, int value) {
        JedisCluster jedis = getConnect();
        try {
            jedis.hincrBy(class1.getName() + SPLITCHAR + id.toString(), field, value);
        } catch (Exception e) {
            log.error("hincrBy fail,", e);

            throw e;
        }
    }

    @Override
    public void hincrBy(String id, Class<?> class1, String field, int value) {
        JedisCluster jedis = getConnect();
        try {
            jedis.hincrBy(class1.getName() + SPLITCHAR + id, field, value);
        } catch (Exception e) {
            log.error("hincrBy fail,", e);
            throw e;
        }
    }

    @Override
    public void hincrBy(Integer id, Class<?> class1, String field, double value) {
        JedisCluster jedis = getConnect();
        try {
            jedis.hincrByFloat(class1.getName() + SPLITCHAR + id.toString(), field, value);
        } catch (Exception e) {
            log.error("hincrBy fail,", e);

            throw e;
        }
    }

    @Override
    public void hincrBy(String id, Class<?> class1, String field, double value) {
        JedisCluster jedis = getConnect();
        try {
            jedis.hincrByFloat(class1.getName() + SPLITCHAR + id, field, value);
        } catch (Exception e) {
            log.error("hincrBy fail,", e);
            throw e;
        }
    }

    @Override
    public void incrBy(String key, int point) {
        JedisCluster jedis = getConnect();
        try {
            jedis.incrBy(key, point);
        } catch (Exception e) {
            log.error("incrBy fail,", e);

            throw e;
        }

    }

    @Override
    public String hget(String key, String field) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.hget(key, field);
        } catch (Exception e) {
            log.error("hget fail,", e);
            throw e;
        }

    }

    @Override
    public Map<String, String> hgetAll(String key) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            log.error("hgetAll fail,", e);
            throw e;
        }
    }

    @Override
    public void hmset(String key, String field, String value) {
        JedisCluster jedis = getConnect();
        try {
            jedis.hset(key, field, value);

        } catch (Exception e) {
            log.error("hmset fail,", e);
            throw e;
        }

    }

    /**
     * 将key设置为空
     *
     * @param key
     */
    @Override
    public void setEmpty(String key) {
        String emptyKey = getEmptyKey(key);
        set(emptyKey, "empty", 60);
    }

    /**
     * 将key设置为空
     *
     * @param key
     */
    @Override
    public void setEmpty(String key, int expire) {
        String emptyKey = getEmptyKey(key);
        set(emptyKey, "empty", expire);
    }

    /**
     * 移除空对象
     *
     * @param key
     */
    @Override
    public void removeEmpty(String key) {
        String emptyKey = getEmptyKey(key);
        delete(emptyKey);
    }

    /**
     * 判断key是否为空
     *
     * @param key
     * @return
     */
    @Override
    public boolean isEmpty(String key) {
        return exsists(getEmptyKey(key));
    }

    private String getEmptyKey(String key) {
        return key + SPLITCHAR + "mark";
    }

    /**
     * 设置对象为空
     *
     * @param key
     * @param class1
     */
    @Override
    public void setEmpty(Integer key, Class<?> class1) {
        String emptyKey = getEmptyKey(key, class1);
        set(emptyKey, "empty");
    }

    /**
     * 设置对象为空
     *
     * @param key
     * @param class1
     */
    @Override
    public void setEmpty(Integer key, Class<?> class1, int expire) {
        String emptyKey = getEmptyKey(key, class1);
        set(emptyKey, "empty", expire);
    }

    /**
     * 移除空对象
     *
     * @param key
     * @param class1
     */
    @Override
    public void removeEmpty(Integer key, Class<?> class1) {
        String emptyKey = getEmptyKey(key, class1);
        delete(emptyKey);
    }

    /**
     * 判断对象是否为空
     *
     * @param key
     * @param class1
     * @return
     */
    @Override
    public boolean isEmpty(Integer key, Class<?> class1) {
        return exsists(getEmptyKey(key, class1));
    }

    private String getEmptyKey(Integer key, Class<?> class1) {
        return class1.getName() + SPLITCHAR + key + SPLITCHAR + "mark";
    }

    @Override
    public Long lock(String key) {
        String lockKey = "_lock_:" + key;
        JedisCluster jedis = getConnect();
        long startTime = System.currentTimeMillis();
        while (jedis.setnx(lockKey, String.valueOf(System.currentTimeMillis())) == 0) {
            if (System.currentTimeMillis() - startTime > 10000) {
                throw new RuntimeException("get lock for key:" + key + " time out");
            }
            log.debug("key:" + key + " is locked, wait 100 ms");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException("lock fail", e);
            }
        }
        jedis.expire(lockKey, 10);
        return 1L;
    }


    private void getlock(JedisCluster jedis, String lockKey, String waitValue, String waitQueueKey, long startTime) {
        while (true) {
            while (jedis.get(lockKey) != null) {
                if (System.currentTimeMillis() - startTime > 10000) {
                    throw new RuntimeException("get lock for waitValue:" + waitValue + " time out");
                }
                if (log.isDebugEnabled()) {
                    log.debug("waitValue:" + waitValue + " is locked, wait 100 ms");
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException("lock fail", e);
                }
            }
            if (Objects.equal(jedis.lindex(waitQueueKey, 0), waitValue)) {
                jedis.lpop(waitQueueKey);
                jedis.set(lockKey, String.valueOf(startTime));
                return;
            }
        }
    }

    @Override
    public Long lockOfFair(String key) {
        String lockKey = "_lock_:" + key;
        String waitQueueKey = "list_queue:" + key;
        JedisCluster jedis = getConnect();
        String waitValue = key + UUID.randomUUID();
        try {
            jedis.rpush(waitQueueKey, waitValue);
            getlock(jedis, lockKey, waitValue, waitQueueKey, System.currentTimeMillis());
            jedis.expire(lockKey, 10);
        } catch (Exception e) {
            log.error("lock fail,", e);
            if (jedis.lrem(waitQueueKey, 0, waitValue) == 0) {
                jedis.del(lockKey);
            }
            throw e;
        }
        return 1L;
    }

    @Override
    public void unlock(String key) {
        String lockKey = "_lock_:" + key;
        JedisCluster jedis = getConnect();
        try {
            jedis.del(lockKey);
        } catch (Exception e) {
            log.error("unlock fail,", e);
            throw e;
        }

    }

    @Override
    public boolean isLocked(String key) {
        String lockKey = "_lock_:" + key;
        JedisCluster jedis = getConnect();
        try {
            return jedis.exists(lockKey);
        } catch (Exception e) {
            log.error("isLocked fail,", e);
            throw e;
        }

    }

    @Override
    public boolean isEmpty(String key, Class<?> class1) {
        String keyEmpty = class1.getName() + SPLITCHAR + key;
        return isEmpty(keyEmpty);
    }

    @Override
    public void setEmpty(String key, Class<?> class1) {
        String keyEmpty = class1.getName() + SPLITCHAR + key;
        setEmpty(keyEmpty);

    }

    @Override
    public void removeEmpty(String key, Class<?> class1) {
        String keyEmpty = class1.getName() + SPLITCHAR + key;
        removeEmpty(keyEmpty);
    }

    @Override
    public Long zRank(String key, String member) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.zrank(key, member);
        } catch (Exception e) {
            log.error("zRank fail,", e);
            throw e;
        }
    }

	@Override
	public void delKeysWithPattern(String pattern) {
		JedisCluster jedis = getConnect();
		Set<String> keys = new HashSet<>();
		try {
			Map<String, JedisPool> clusterNodes = jedis.getClusterNodes();
			for (String nodeKey : clusterNodes.keySet()) {
				log.debug("Getting keys from: {}", nodeKey);
				JedisPool jp = clusterNodes.get(nodeKey);
				Jedis connection = jp.getResource();
				try {
					keys.addAll(connection.keys(pattern));
				} catch (Exception e) {
					log.error("Getting keys error: {}", e);
				} finally {
					log.debug("Connection closed.");
					connection.close();
				}
			}
			for (String key : keys) {
				delete(key);
			}
		} catch (Exception e) {
			log.error("delKeysWithPattern fail,", e);
			throw e;
		}
	}
	
	@Override
    public String lindex(String key, long index) {
        JedisCluster jedis = getConnect();
        try {
            return jedis.lindex(key, index);
        } catch (Exception e) {
            log.error("lindex fail,", e);
            throw e;
        }
    }
}
