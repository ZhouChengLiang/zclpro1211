package org.zclpro.service.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.zclpro.common.redistool.MyJedisCommands;
import org.zclpro.db.common.IPOJOCache;
import org.zclpro.db.common.KeySortNum;

public abstract class BaseCache {

    @Autowired
    protected MyJedisCommands redisCache;

    public abstract int getExpire();

    /**
     * 缓存对象
     *
     * @param obj
     * @return 如果命令执行成功，返回 OK
     * @throws NullPointerException param == null
     */
    public String cachePOJO(IPOJOCache obj) {
        if (obj == null)
            throw new NullPointerException();
        if (getExpire() != 0)
            return redisCache.hmset(obj.getKey().toString(), obj, getExpire());
        return redisCache.hmset(obj.getKey().toString(), obj);
    }

    /**
     * 判断（缓存本身和空标记同时不存在）
     * <p>只有同时不存在时，才从数据库加载填充缓存</p>
     *
     * @param key
     * @return true 表示同时不存在，需要从数据库填充缓存
     * false（缓存存在或者数据库为空）不需要从数据库填充缓存
     */
    public boolean needFillCache(String key) {
        if ((!redisCache.exsists(key)) && (!redisCache.isEmpty(key)))
            return true;
        return false;
    }

    /**
     * 删除缓存中的对象
     *
     * @param key
     * @param clazz
     * @return
     */
    public long delPOJO(String key, Class<?> clazz) {
        if (key == null)
            throw new NullPointerException();
        return redisCache.delete(key, clazz);
    }

    /**
     * 从缓存中取出hash
     *
     * @param key
     * @param clazz
     * @return
     * @throws NullPointerException key == null || clazz == null
     */
    public <T extends IPOJOCache> T getPoJOFromCache(String key, Class<T> clazz) {
        if (key == null || clazz == null)
            throw new NullPointerException();
        return redisCache.hmget(key, clazz);
    }

    /**
     * 从缓存中取出hash特定字段值
     *
     * @param key
     * @param clazz
     * @param fieldName
     * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回null
     * @throws NullPointerException (key == null) || (clazz == null) || (fieldName == null)
     */
    public String getFieldValueFromPOJOCache(String key, Class<?> clazz, String fieldName) {
        if (key == null || clazz == null || fieldName == null)
            throw new NullPointerException();
        return redisCache.hget(key, clazz, fieldName);
    }

    public Long lock(String key) {
        return redisCache.lock(key);
    }

    public void unlock(String key) {
        redisCache.unlock(key);
    }

    /**
     * 通用的用list填充有序集合工具方法
     *
     * @param keySortNumList
     * @return 成功插入缓存的数量 0 填充失败，表示缓存不存在，并设置空标记 >0 表示填充成功
     */
    public long zAddKeySortList(String key, List<KeySortNum> keySortNumList) {
        if (CollectionUtils.isEmpty(keySortNumList)) {
            redisCache.setEmpty(key);
            return 0;
        }
        Map<String, Double> keySortMap = new HashMap<>();
        for (KeySortNum each : keySortNumList) {
            keySortMap.put(String.valueOf(each.getId()), (double) each.getSortNum());
        }
        return redisCache.zadd(key, keySortMap);
    }

    /**
     * 有序集合缓存不存在，统一填充方法
     *
     * @param key
     * @param supplier
     * @return 0 不需要填充缓存(数据库为空或者空标记存在) -1 数据库为空  其他为填充缓存对象具体数量
     */
    public long needThanFillZsort(String key, Supplier<List<KeySortNum>> supplier) {
        if (!needFillCache(key))
            return 0;
        List<KeySortNum> keySortNumList = supplier.get();
        if (CollectionUtils.isEmpty(keySortNumList)) {
            redisCache.setEmpty(key,5);
            return -1;
        }
        Map<String, Double> keySortMap = new HashMap<>();
        for (KeySortNum each : keySortNumList) {
            keySortMap.put(String.valueOf(each.getId()), (double) each.getSortNum());
        }
        return redisCache.zadd(key, keySortMap);
    }
}
