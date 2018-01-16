/*
 * @date 2017年03月30日 下午3:13
 */
package com.icourt.core.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author june
 */
public class JsonService implements IJsonService{

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    @PostConstruct
    public void init(){
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    @Override
    public Map<String, Object> parseObject(String jsonStr) {
        return parseObject(jsonStr,Map.class);
    }

    @Override
    public <T> T parseObject(String jsonStr, Class<T> clazz) {
        return JSON.parseObject(jsonStr,clazz);
    }

    @Override
    public <T> List<T> parseArray(String jsonStr, Class<T> clazz) {
        return JSON.parseArray(jsonStr,clazz);
    }

    @Override
    public String toJSONString(Object object) {
        return JSON.toJSONString(object,SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public boolean isValidJson(String jsonStr) {
        try {
            JSON.parseObject(jsonStr);
            return true;
        }catch (Exception ignored){}
        return false;
    }

    @Override
    public <T> byte[] serialize(T t) {
        if (t == null) {
            return new byte[0];
        }
        try (SerializeWriter out = new SerializeWriter()) {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.SkipTransientField, false);
            serializer.config(SerializerFeature.WriteClassName, true);
            serializer.config(SerializerFeature.WriteMapNullValue, true);
            serializer.write(t);
            return out.toString().getBytes(DEFAULT_CHARSET);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes,Class<T> clazz) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(str, clazz);
    }
}
