/**
 * @date 2016年09月26日 14:01
 */
package com.icourt.data.redis;

import com.icourt.common.StrKit;
import com.icourt.data.ICache;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存
 * XXXOperations 获取一个operator，但是没有指定操作的对象（key），每次操作都得指定key
 * BoundXXXXOperations 获取了一个指定操作对象（key）的operator，每次操作就不用再指定key了
 * ValueOperations：简单K-V操作
 * SetOperations：set类型数据操作
 * ZSetOperations：zset类型数据操作
 * HashOperations：针对map类型的数据操作
 * ListOperations：针对list类型的数据操作
 * @author june
 */
public class RedisCache implements ICache {

    private RedisTemplate template;
    //非http线程里使用请记得调用 destroy()方法删除ThreadLocal
    //有个小风险，如果线程运行过程中key已失效了，可能存在脏读
    private static final ThreadLocal<Map<String,Object>> objectThreadLocal = new ThreadLocal<>();

    @Override
    public <T> T get(String key) {
        Map<String,Object> map = objectThreadLocal.get();
        Object object = null;
        if(map!=null){
            object = map.get(key);
        }else{
            map = new HashMap<>();
        }
        if(object == null && !map.containsKey(key)){
            T t = (T) template.boundValueOps(key).get();
            map.put(key,t);
            objectThreadLocal.set(map);
            return t;
        }
        return  (T) object;
    }

    @Override
    public void set(String key, Object obj, int expire) {
        template.boundValueOps(key).set(obj,expire, TimeUnit.SECONDS);
        removeCache(key);
    }

    @Override
    public void set(String key, Object obj) {
        template.boundValueOps(key).set(obj);
        removeCache(key);
    }

    @Override
    public Boolean setIfNotExists(String key, Object obj) {
        removeCache(key);
        return template.boundValueOps(key).setIfAbsent(obj);
    }

    @Override
    public Long delKey(String... keys) {
        if(keys==null || keys.length==0){
            return 0L;
        }
        removeCache(keys);
        final byte[][] rawKeys = rawKeys(keys);
        return (Long) template.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) {
                return connection.del(rawKeys);
            }
        }, true);
    }

    @Override
    public Long delKeysLike(String... likeKeys) {
        if(likeKeys==null || likeKeys.length==0){
            return 0L;
        }
        Set<String> allKeys = new HashSet<>();
        for (String likeKey : likeKeys) {
            //所有key
            Set<String> keys = template.keys("*" + likeKey + "*");
            allKeys.addAll(keys);
        }
        destroy();
        return delKey(allKeys.toArray(new String[0]));
    }

    @Override
    public Boolean expire(String key, int expire) {
        return template.expire(key,expire, TimeUnit.SECONDS);
    }


    @Override
    public long ttl(final String key) {
        return (long) template.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ttl(rawKey(key));
            }
        });
    }

    /**
     * 删除某个缓存的key
     * @param keys key
     */
    private void removeCache(String... keys){
        if(keys==null){
            return;
        }
        Map<String,Object> map = objectThreadLocal.get();
        if(map!=null){
            for (String key : keys) {
                map.remove(key);
            }
            objectThreadLocal.set(map);
        }
    }

    @Override
    public void destroy() {
        objectThreadLocal.remove();
    }


    /**
     * 序列化key
     * @param key key
     * @return byte[]
     */
    private byte[] rawKey(String key){
        if(StrKit.isEmpty(key)){
            return null;
        }
        return template.getStringSerializer().serialize(key);
    }

    /**
     * 批量序列化key
     * @param keys keys
     * @return byte[][]
     */
    private byte[][] rawKeys(String[] keys){
        if(keys==null || keys.length==0){
            return null;
        }
        byte[][] rawKeys = new byte[keys.length][];
        int i = 0;
        for (String key : keys) {
            rawKeys[i++] = rawKey(key);
        }
        return rawKeys;
    }

    public RedisTemplate getTemplate() {
        return template;
    }

    public void setTemplate(RedisTemplate template) {
        this.template = template;
    }
}
