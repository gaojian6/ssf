/*
 * @date 2017年03月30日 下午3:13
 */
package com.icourt.core.json;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.icourt.common.BeanKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * json工具
 * @author june
 */
public class JsonKit {

    private final static Logger logger = LoggerFactory.getLogger(JsonKit.class);

    /**
     * json字符串转对象
     * @param jsonStr json字符串
     * @return map
     */
    public static Map<String,Object> parseObject(String jsonStr){
        return getJsonService().parseObject(jsonStr);
    }

    /**
     * json字符串转对象
     * @param jsonStr json字符串
     * @param clazz class
     * @param <T> 对象类型
     * @return t
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz){
        return getJsonService().parseObject(jsonStr,clazz);
    }

    /**
     * json字符串转对象集合
     * @param jsonStr json字符串
     * @param clazz class
     * @param <T> 对象类型
     * @return list
     */
    public static <T> List<T> parseArray(String jsonStr, Class<T> clazz){
        return getJsonService().parseArray(jsonStr,clazz);
    }


    /**
     * 是否是有效json字符串
     * @param jsonStr  json字符串
     * @return true 是 false 不是
     */
    public static boolean isValidJson(String jsonStr){
        return getJsonService().isValidJson(jsonStr);
    }

    /**
     * 对象转json字符串
     * @param object 对象
     * @return json字符串
     */
    public static String toJSONString(Object object){
        return getJsonService().toJSONString(object);
    }

    /**
     * 获取jsonService
     * @return IJsonService
     */
    private static IJsonService getJsonService(){
        IJsonService jsonService;
        if(BeanKit.getApplicationContext() == null){
            jsonService = new JsonService();
            logger.warn("请初始化spring后再使用JsonKit");
        }else{
            jsonService = BeanKit.getBean(IJsonService.class);
        }
        return jsonService;
    }

}
