/**
 * 
 */
package com.icourt.data.redis;


import com.icourt.common.BeanKit;
import com.icourt.data.ICache;

import java.util.Random;

/**
 * redis实现的跨jvm的lock
 * @author june
 */
public class RedisLock {

    private static final String cacheBeanName = "redisCache";

    /** 加锁标志 */  
    public static final String LOCKED = "TRUE";
    /** 毫秒与毫微秒的换算单位 1毫秒 = 1000000毫微秒 */  
    public static final long MILLI_NANO_CONVERSION = 1000 * 1000L;  
    /** 默认超时时间（毫秒） */  
    public static final long DEFAULT_TIME_OUT = 1000;  
    public static final Random RANDOM = new Random();
    /** 锁的超时时间（秒），过期删除 */  
    public static final int EXPIRE = 3 * 60;  
  
    private String key;
    // 锁状态标志  
    private boolean locked = false;  
  
    /** 
     * This creates a RedisLock 
     * @param key key 
     */  
    public RedisLock(String key) {
        this.key = key + "_lock";  
    }
    
    /**
     * 加锁 
     * 应该以： 
     * lock(); 
     * try { 
     *      doSomething(); 
     * } finally { 
     *      unlock()； 
     * } 
     * 的方式调用  
     * @param timeout 超时时间 
     * @return 成功或失败标志 
     */  
    public boolean lock(long timeout) {  
        return lock(timeout,EXPIRE);
    }  
  
    /** 
     * 加锁 
     * 应该以： 
     * lock(); 
     * try { 
     *      doSomething(); 
     * } finally { 
     *      unlock()； 
     * } 
     * 的方式调用 
     * @param timeout 超时时间 
     * @param expire 锁的时间（秒），过期删除
     * @return 成功或失败标志 
     */  
    public boolean lock(long timeout, int expire) {  
        ICache cache = getCache();
        long nano = System.nanoTime();
        timeout *= MILLI_NANO_CONVERSION;
        try {
            while ((System.nanoTime() - nano) < timeout) {
                if(cache.setIfNotExists(this.key,LOCKED)){
                    cache.expire(this.key,expire);
                    this.locked = true;
                    return true;
                }
                // 短暂休眠，避免出现活锁  
                Thread.sleep(3, RANDOM.nextInt(500));
            }  
        } catch (Exception e) {
            throw new RuntimeException("Locking error", e);
        }
        return false;  
    }  
  
    /** 
     * 加锁 
     * 应该以： 
     * lock(); 
     * try { 
     *      doSomething(); 
     * } finally { 
     *      unlock()； 
     * } 
     * 的方式调用 
     * @return 成功或失败标志 
     */  
    public boolean lock() {  
        return lock(DEFAULT_TIME_OUT);  
    }  
  
    /** 
     * 解锁 
     * 无论是否加锁成功，都需要调用unlock 
     * 应该以： 
     * lock(); 
     * try { 
     *      doSomething(); 
     * } finally { 
     *      unlock()； 
     * } 
     * 的方式调用 
     */  
    public void unlock() {
        if (this.locked) {
            getCache().delKey(this.key);
        }
    }

    /**
     * 获取缓存
     * @return cache
     */
    private static ICache getCache(){
        ICache cache = BeanKit.getBean(cacheBeanName,ICache.class);
        if(cache==null){
            throw new RuntimeException("请在初始化redis后再使用");
        }
        return cache;
    }

}
