/*
 * @date 2017年03月30日 下午3:12
 */
package com.icourt.core.json;

import java.util.List;
import java.util.Map;

/**
 * @author june
 */
public interface IJsonService {

    /**
     * json字符串转对象
     * @param jsonStr json字符串
     * @return map
     */
    Map parseObject(String jsonStr);

    /**
     * json字符串转对象
     * @param jsonStr json字符串
     * @param clazz class
     * @param <T> 对象类型
     * @return t
     */
    <T> T parseObject(String jsonStr, Class<T> clazz);

    /**
     * json字符串转对象集合
     * @param jsonStr json字符串
     * @param clazz class
     * @param <T> 对象类型
     * @return list
     */
    <T> List<T> parseArray(String jsonStr, Class<T> clazz);

    /**
     * 对象转json字符串
     * @param object 对象
     * @return json字符串
     */
    String toJSONString(Object object);

    /**
     * 是否是有效json字符串
     * @param jsonStr  json字符串
     * @return true 是 false 不是
     */
    boolean isValidJson(String jsonStr);

    /**
     * 序列化
     * @param t 对象
     * @param <T> 类型
     * @return byte[]
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化
     * @param bytes bytes
     * @param clazz class
     * @param <T> 对象列席
     * @return 对象
     */
    <T> T deserialize(byte[] bytes,Class<T> clazz);




}
