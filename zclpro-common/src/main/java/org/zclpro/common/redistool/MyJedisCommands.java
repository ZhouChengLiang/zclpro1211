package org.zclpro.common.redistool;

import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MyJedisCommands {

	String SPLITCHAR = ":";
	/**
	 * get
	 * 
	 * @param key
	 * @return
	 */
	byte[] get(byte[] key) ;

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
	Long zadd(String key, double score, String member) ;

	Long zadd(String key, Map<String, Double> scoreMembers) ;

	/**
	 * 仅向有序集合中添加元素，不会进行集合截取操作
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	Long zaddWithOutRem(String key, double score, String member);

	Set<String> zrangeByScore(final String key, final long min, final long max);
	Set<String> zrangeByScore(final String key, final String min, final String max);

	/**
	 * Redis Zrange 返回有序集中，指定区间内的成员。
	 * <p>
	 * 其中成员的位置按分数值递增(从小到大)来排序 具有相同分数值的成员按字典序来排列
	 * <p> 下标参数 start 和 stop 都以 0 为底，也就是说，以 0
	 * 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推 你也可以使用负数下标，以 -1 表示最后一个成员， -2
	 * 表示倒数第二个成员，以此类推
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	Set<String> zrange(final String key, final long start, final long end);
	/**
	 * <p>返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序
	 * <p> 下标参数 start 和 stop 都以 0 为底，也就是说，以 0
	 * 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推 你也可以使用负数下标，以 -1 表示最后一个成员， -2
	 * 表示倒数第二个成员，以此类推
	 * @param key
	 * @param start
	 * @param end
	 * @return 有序集 key 的成员
	 */
	Set<String> zrevrange(final String key, final long start, final long end) ;

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
	Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset,
			final int count);

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
	Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset,
			final int count);
	/**
	 * 返回有序集合中指定分数区间内的成员，分数由高到低排序
	 * @param key
	 * @param max
	 * @param min
	 * @param offset
	 * @param count
	 * @return
	 */
	Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset,
			final int count) ;

	/**
	 * 返回有序集合中指定分数区间内的成员，分数由高到低排序
	 * 
	 * @param key
	 * @param max
	 * @param min
	 * @return
	 */
	Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min);

	/**
	 * <p>
	 * 移除有序集中的一个或多个成员,不存在的成员将被忽略
	 * 
	 * @param key
	 * @param members
	 * @return 被成功移除的成员的数量，不包括被忽略的成员。
	 */
	long zRemove(final String key, final String... members);

	/**
	 * get
	 * @param key
	 * @return
	 */
	String get(String key);

	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	String set(byte[] key, byte[] value);

	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	String set(String key, String value);

	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return Status code reply
	 */
	String set(byte[] key, byte[] value, int expire) ;

	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return Status code reply
	 */
	String set(String key, String value, int expire);
	/**
	 * <P>
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
	String hmset(String key, Object value, int expire);

	/**
	 * hmset 将对象设置成map，设置到redis hash表中
	 * 
	 * @param key
	 * @param value
	 * @return Status code reply
	 */
	String hmset(String key, Object value) ;

	/**
	 * hmset 将对象设置成map，设置到redis hash表中
	 * 
	 * @param key
	 * @return Status code reply
	 */
	<T> T hmget(String key, Class<T> clazz);
	/**
	 *命令用于返回哈希表中指定字段的值 
	 * @param key
	 * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回null
	 */
	String hget(String key, Class<?> clazz, String fieldName);

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
	Long hset(final String key, final String field, final String value);

	/**
	 * 设置对象obj中的field字段
	 * 
	 * @param key
	 * @param obj
	 * @param field
	 * @return
	 */
	Long hset(final String key, Object obj, final String field);

	/**
	 * 修改hash存储中的字段值
	 * @param key
	 * @param clazz
	 * @param fieldName
	 * @param value
     * @return
     */
	Long hsetObjectFieldValue(final String key, Class<?> clazz, final String fieldName, final Object value);
	
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
	Long listRpush(final String key, final String... strings);
	/**
	 * <p>
	 * 用于将一个或多个值插入到列表的尾部(左边)。
	 * <p>
	 * 如果列表不存在，一个空列表会被创建并执行 此 操作。 当列表存在但不是列表类型时，返回一个错误。
	 * 
	 * @param key
	 * @param strings
	 * @return 执行  操作后，列表的长度
	 */
	Long listLpush(final String key, final String... strings);
	/**
	 * <p>
	 * 用于将一个或多个值插入到列表的尾部(左边)。
	 * <p>
	 * 如果列表不存在，一个空列表会被创建并执行 此 操作。 当列表存在但不是列表类型时，返回一个错误。
	 * @param key
	 * @param strings
	 * @param remain list集合保留的长度 (保留最新值)
	 * @return 执行  操作后，列表的长度
	 */
	Long listLpushAndFixedLength(final String key, int remain ,final String... strings);
	/**
	 * <p>移除列表中与参数 record 相等的元素
	 * @param key
	 * @param record
	 * @return 被移除元素的数量。 列表不存在时返回 0 。
	 */
	Long listremove(final String key,final String record);
	
	

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
	List<String> listLrange(final String key, final long start, final long end);
	/**
	 * <p>
	 * 用于将一个或多个值插入到列表的尾部(右边边)，从左截取remain个。
	 * <p>
	 * 如果列表不存在，一个空列表会被创建并执行 此 操作。 当列表存在但不是列表类型时，返回一个错误。
	 * @param key
	 * @param strings
	 * @param remain list集合保留的长度 (保留最新值)
	 * @return 执行  操作后，列表的长度
	 */
	Long listRpushAndFixedLength(final String key, int remain ,final String... strings);

	/**
	 * delele
	 * 
	 * @param key
	 */
	Long delete(byte[] key) ;
	
	/**
	 * 根据key删除记录
	 * @param key
	 */
	Long delete(String key) ;


	void expire(String key, int expire);

	/**
	 * 删除hash存储的对象
	 * 
	 * @param key
	 * @param clazz 对象的运行时类型
	 */
	Long delete(String key, Class<?> clazz);
	Long ttl(String key) ;

	/**
	 * 判断member是否是set的一个元素，如果是返回true，否则返回false
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	Boolean sismember(String key, String member);
	
	/**
	 * 
	 * 向集合中添加元素（或数组）
	 * @param key
	 * @param members
	 * @return
	 */
	Long sadd(String key, String... members);
	
	/**
	 * 
	 * 从集合中移除记录
	 * @param key
	 * @param members
	 * @return
	 */
	Long srem(String key, String... members) ;
	
	/**
	 * 
	 * 返回集合中的元素
	 * @param key
	 * @return
	 */
	Set<String> smembers(String key);

	/**
	 * 判断是否存在key
	 * 
	 * @param key
	 * @return
	 */
	boolean exsists(String key);
	boolean exsists(byte[] key);
	boolean exsists(Integer id, Class<?> class1);

	/**
	 * 返回无序集合的元素个数
	 * @param key
	 * @return
	 */
	Long scard(String key) ;
	
	/**
	 * 返回有序集合的元素个数
	 * @param key
	 * @return
	 */
	Long zcard(String key);

	/**
	 * 返回有序集合的在min和max分数之间的元素个数
	 * @param key
	 * @param min
	 * @param max
     * @return
     */
	Long zcount(String key, Long min, long max) ;
	
	void incr(String key);
	
	Long hincrBy(String key, String field, int value);
	
	void hincrBy(Integer id, Class<?> class1, String field, int value);
	
	void hincrBy(Integer id, Class<?> class1, String field, double value);
	
	void hincrBy(String id, Class<?> class1, String field, int value);
	
	void hincrBy(String id, Class<?> class1, String field, double value);

	void incrBy(String key, int point);

	String hget(String key, String field);

	Map<String, String> hgetAll(String key);

	void hmset(String key, String field, String value);
	
	
	/**
	 * 将key设置为空
	 * @param key
	 */
	void setEmpty(String key);
	/**
	 * 将key设置为空,默认时效为30s
	 * @param key
	 */
	void setEmpty(String key, int expire);
	/**
	 * 移除空对象
	 * @param key
	 */
	void removeEmpty(String key);

	/**
	 * 判断key是否为空
	 * @param key
	 * @return
	 */
	boolean isEmpty(String key);
	

	/**
	 * 设置对象为空 
	 * @param key
	 * @param class1
	 */
	void setEmpty(Integer key, Class<?> class1) ;
	/**
	 * 将key设置为空,默认时效为30s
	 * @param key
	 */
	void setEmpty(Integer key, Class<?> class1, int expire);
	/**
	 * 移除空对象
	 * @param key
	 * @param class1
	 */
	void removeEmpty(Integer key, Class<?> class1);
	/**
	 * 判断对象是否为空
	 * @param key
	 * @param class1
	 * @return true 代表空标记存在(不需要填充缓存) fasle 需要填充缓存
	 */
	boolean isEmpty(Integer key, Class<?> class1);

	Long lock(String key);

	/**
	 * 公平锁
	 * @param key
	 * @return
	 */
	Long lockOfFair(String key);

	void unlock(String key);

	boolean isLocked(String key);

	boolean isEmpty(String key, Class<?> class1);

	void setEmpty(String key, Class<?> class1);

	void removeEmpty(String key, Class<?> class1);

	Long zRank(String key, String member);
	
	void delKeysWithPattern(String pattern);
	
	String lindex(String key, long index);
}
