/**
 * @date 2016年09月26日 11:28
 */
package com.icourt.data.redis;

import com.icourt.core.json.IJsonService;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 使用json序列化
 * @author june
 */
public class JsonRedisSerializer<T> implements RedisSerializer<T> {

    private IJsonService jsonService;

    private Class<T> clazz;

    public JsonRedisSerializer(Class<T> clazz,IJsonService jsonService) {
        super();
        this.clazz = clazz;
        this.jsonService = jsonService;
    }

    public byte[] serialize(T t) throws SerializationException {
        return jsonService.serialize(t);
    }

    public T deserialize(byte[] bytes) throws SerializationException {
        return jsonService.deserialize(bytes,clazz);
    }

}
