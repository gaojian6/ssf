/*
 * @date 2016年09月26日 14:03
 */
package com.icourt.data;

/**
 * 缓存
 * @author june
 */
public interface ICache {

    /**
     * 根据key获取对象
     * @param key key
     * @param <T> 对象类型
     * @return val
     */
    <T> T get(String key);
    /**
     * 缓存一个对象
     * @param key key
     * @param obj value
     * @param expire 过期时间，单位 秒
     */
    void set(String key, Object obj, int expire);

    /**
     * 永久缓存一个对象
     * @param key key
     * @param obj val
     */
    void set(String key, Object obj);
    /**
     * 将 key 的值设为 obj ，当且仅当 key 不存在。若给定的 key 已经存在，则 setIfNotExists 不做任何动作。
     * @param key key
     * @param obj value
     * @return 设置成功，返回 tru 。设置失败，返回 false 。
     */
    Boolean setIfNotExists(String key, Object obj);
    /**
     * 删除
     * @param keys 精确匹配的key
     * @return 删除成功的条数
     */
    Long delKey(String... keys);

    /**
     * 根据key批量模糊删除
     * @param likeKeys keys
     * @return 删除成功的条数
     */
    Long delKeysLike(String... likeKeys);

    /**
     * 设置key的有效时间
     * @param key key
     * @param expire 有效时间，秒
     * @return 成功true，失败false
     */
    Boolean expire(String key, int expire);

    /**
     * 获取某个key的生存时间
     * @param key key
     * @return 如果永久有效返回-1 单位秒,不存在的key返回-2
     */
    long ttl(String key);

    /**
     * 线程结束的时候调用
     */
    void destroy();
}
