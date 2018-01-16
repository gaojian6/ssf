/*
 * @date 2016年09月26日 19:29
 */
package com.icourt.data.redis;

import com.icourt.core.json.IJsonService;
import com.icourt.data.ICache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;

/**
 * redis配置
 * @author june
 */
@ConditionalOnClass({ JedisConnection.class, RedisOperations.class, Jedis.class })
public class ApplicationRedisAutoConfiguration {

    /**
     * 使用json序列化
     * @return RedisSerializer
     */
    @Bean
    public RedisSerializer jsonRedisSerializer(IJsonService jsonService) {
        return new JsonRedisSerializer<>(Object.class,jsonService);
    }

    @SuppressWarnings("unchecked")
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    @Primary
    public RedisTemplate redisTemplate(RedisConnectionFactory factory,IJsonService jsonService){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setValueSerializer(jsonRedisSerializer(jsonService));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(jsonRedisSerializer(jsonService));
        redisTemplate.setHashValueSerializer(jsonRedisSerializer(jsonService));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "redisCache",value = ICache.class)
    public ICache redisCache(RedisTemplate redisTemplate){
        RedisCache redisCache = new RedisCache();
        redisCache.setTemplate(redisTemplate);
        return redisCache;
    }
}
