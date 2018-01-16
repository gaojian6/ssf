package com.icourt.orm.dao.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.icourt.common.ClassKit;
import com.icourt.common.StrKit;
import com.icourt.orm.dao.annotation.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体类相关工具类
 *
 * @author june
 */
public class PojoKit {

    /**
     * 根据map的值 构建pojo
     *
     * @param t
     * @param resultMap
     * @return
     */
    public static <T> T build(T t, Map<String, Object> resultMap) {
        if (t == null || resultMap == null || resultMap.isEmpty()) {
            return t;
        }
        //查询所有属性值
        Map<String, Class<?>> fieldMap = ClassKit.getFieldMap(t.getClass());
        List<Field> fieldList = ClassKit.getAllFields(t.getClass(), null);
        for (Field field : fieldList) {
            String fieldName = field.getName();
            Class<?> clazz = fieldMap.get(fieldName);
            Object obj = null;
            try {
                if (!ClassKit.isBaseDataType(clazz)) {
                    obj = ClassKit.newInstance(clazz);
                }
            } catch (Exception e) {
            }
            //普通非pojo属性
            if (obj == null) {
                String columnName;
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    columnName = column.name();
                } else {
                    columnName = fieldName;
                }
                if (resultMap.containsKey(columnName)) {
                    Object value = ClassKit.getObjectValue(resultMap.get(columnName), field.getType().getName());
                    //注入属性值
                    ClassKit.inject(t, fieldName, value);
                }
            } else {
                //注入关联的pojo
                Map<String, Object> newResultMap = new HashMap<String, Object>();
                String[] keys = resultMap.keySet().toArray(new String[0]);
                for (String key : keys) {
                    String prefix = fieldName + ".";
                    //如果存在key值为类似user.person的key,则认为是关联了pojo
                    if (key.startsWith(prefix) && key.length() > prefix.length()) {
                        String newFieldName = key.substring(prefix.length(), key.length());
                        newResultMap.put(newFieldName, resultMap.get(key));
                    }
                }
                if (!newResultMap.isEmpty()) {
                    build(obj, newResultMap);
                    ClassKit.inject(t, fieldName, obj);
                }
            }
        }

        return t;
    }

    /**
     * 根据map构建pojo
     *
     * @param clazz
     * @param resultMap
     * @return
     */
    public static <T> T build(Class<T> clazz, Map<String, Object> resultMap) {
        if (clazz == null || resultMap == null) {
            return null;
        }
        return build(ClassKit.newInstance(clazz), resultMap);
    }

    /**
     * 根据map构建pojo集合
     *
     * @param clazz
     * @param resultList
     * @return
     */
    public static <T> List<T> build(Class<T> clazz, List<Map<String, Object>> resultList) {
        List<T> ts = new ArrayList<>();
        for (Map<String, Object> resultMap : resultList) {
            ts.add(build(clazz, resultMap));
        }
        return ts;
    }


    /**
     * json字符串转Pojo
     *
     * @param <T>
     * @param t
     * @param jsonStr
     * @return
     */
    public static <T> T build(T t, String jsonStr) {
        if (t == null || StrKit.isEmpty(jsonStr)) {
            return t;
        }
        if (jsonStr.indexOf("[") == 0) {
            t = (T) JSON.parseArray(jsonStr, t.getClass()).get(0);
        } else {
            t = (T) JSON.parseObject(jsonStr, t.getClass());
        }
        return t;
    }

    /**
     * json字符串转Pojo
     *
     * @param clazz
     * @param jsonStr
     * @param <T>
     * @return
     */
    public static <T> T build(Class<T> clazz, String jsonStr) {
        if (clazz == null || StrKit.isEmpty(jsonStr)) {
            return null;
        }
        return build(ClassKit.newInstance(clazz), jsonStr);
    }

    /**
     * 实体类转map
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> getMap(T t) {
        String jsonStr = JSON.toJSONString(t,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect);
        return JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
        });
    }

}
