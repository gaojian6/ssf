/*
 * @date 2017年04月10日 上午11:40
 */
package com.icourt.orm.mybatis;

import com.icourt.data.CacheTimeKit;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * mybatis二级缓存 redis实现
 * @author june
 */
public class MybatisRedisCache implements Cache{

    private static final String PREFIX = "ssf:mybatis:";
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);


    private static RedisTemplate<Object,Object> template;


    private String id;

    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = PREFIX+id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        template.boundHashOps(id).put(key,value);
        template.boundHashOps(id).expire(CacheTimeKit.OneMonth, TimeUnit.SECONDS);
    }

    @Override
    public Object getObject(Object key) {
        return template.boundHashOps(id).get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return template.boundHashOps(id).delete(key);
    }

    @Override
    public void clear() {
        for (Object key : template.boundHashOps(id).keys()) {
            removeObject(key);
        }
    }

    @Override
    public int getSize() {
        return Math.toIntExact(template.boundHashOps(id).size());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    public static void setRedisTemplate(RedisTemplate<Object,Object> redisTemplate){
        template = redisTemplate;
    }

}
