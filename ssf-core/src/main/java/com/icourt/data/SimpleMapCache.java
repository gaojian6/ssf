/*
 * @date 2016年10月27日 17:35
 */
package com.icourt.data;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用map实现本地缓存
 * @author june
 */
public class SimpleMapCache implements ICache {

    // 默认的缓存容量
    private static int DEFAULT_CAPACITY = 2048;

    // 使用默认容量创建一个Map
    private ConcurrentHashMap<String, CacheEntity> cache = new ConcurrentHashMap<>(DEFAULT_CAPACITY);

    private class CacheEntity{
        /**
         * 值
         */
        private Object value;

        /**
         * 保存的时间戳
         */
        private long gmtModify;

        /**
         * 过期时间，精确到秒,-1永久有效,-2找不到key
         */
        private int expire;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        private long getGmtModify() {
            return gmtModify;
        }

        private int getExpire() {
            return expire;
        }

        private CacheEntity(Object value, long gmtModify, int expire) {
            super();
            this.value = value;
            this.gmtModify = gmtModify;
            this.expire = expire;
        }
    }
    @Override
    public <T> T get(String key) {
        CacheEntity cacheEntity = cache.get(key);
        if (cacheEntity == null) {
            return null;
        }
        if (checkTimeExpire(key, cacheEntity)) {
            return null;
        }
        return (T) cache.get(key).getValue();
    }

    @Override
    public void set(String key, Object obj, int expire) {
        CacheEntity entityClone = new CacheEntity(obj, System.currentTimeMillis(), expire);
        cache.put(key, entityClone);
    }

    @Override
    public void set(String key, Object obj) {
        set(key,obj,-1);
    }

    @Override
    public Boolean setIfNotExists(String key, Object obj) {
        Object old = get(key);
        if(old!=null){
            return false;
        }
        set(key,obj);
        return true;
    }

    @Override
    public Long delKey(String... keys) {
        Long count = 0L;
        for (String key : keys) {
            Object obj = get(key);
            if(obj!=null){
                count++;
            }
            cache.remove(key);
        }
        return count;
    }

    @Override
    public Long delKeysLike(String... likeKeys) {
        Long count = 0L;
        for (String key : cache.keySet()) {
            for (String likeKey : likeKeys) {
                if(key.contains(likeKey)){
                    cache.remove(key);
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public Boolean expire(String key, int expire) {
        Object obj = get(key);
        if(obj==null){
            return false;
        }
        set(key,obj,expire);
        return true;
    }

    @Override
    public long ttl(String key) {
        CacheEntity cacheEntity = cache.get(key);
        //不存在
        if (cacheEntity == null) {
            return -2;
        }
        //已存活时间
        int live = (int) ((System.currentTimeMillis() - cacheEntity.getGmtModify()) / 1000);
        //过期时间
        int expire = cacheEntity.getExpire();
        //永久有效
        if (expire == -1) {
            return -1;
        }
        //已经过期
        if (checkTimeExpire(key, cacheEntity)) {
            return -2;
        }
        return expire-live;
    }

    @Override
    public void destroy() {

    }

    /**
     * 过期缓存的具体处理方法， true 过期， false 未过期
     */
    private boolean checkTimeExpire(String key, CacheEntity cacheEntity) {
        int expire = cacheEntity.getExpire();
        if (expire == -1) {
            return false;
        }
        int live = (int) ((System.currentTimeMillis() - cacheEntity.getGmtModify()) / 1000);
        if (live > expire) {
            cache.remove(key);
            return true;
        } else {
            return false;
        }
    }
}
