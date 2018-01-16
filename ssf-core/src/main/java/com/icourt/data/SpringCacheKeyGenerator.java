/*
 * @date 2017年04月08日 上午10:58
 */
package com.icourt.data;

import com.icourt.common.ClassKit;
import com.icourt.common.HashKit;
import com.icourt.core.json.IJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存key的生成
 * @author june
 */
public class SpringCacheKeyGenerator implements KeyGenerator{

    @Autowired
    private IJsonService jsonService;

    private static final String NULL_PARAM_KEY = "null_key";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getName());
        sb.append("_");
        sb.append(method.getName());
        sb.append("_");
        List<Object> paramList = new ArrayList<>();
        if(params != null){
            for (Object param : params) {
                if(param == null){
                    param = NULL_PARAM_KEY;
                }
                if(ClassKit.isBaseDataType(param.getClass())){
                    paramList.add(param.toString()+param.getClass().getName());
                }else {
                    paramList.add(param);
                }
            }
            String paramStr = jsonService.toJSONString(paramList);
            sb.append(HashKit.md5(paramStr));
        }
        return sb.toString();
    }
}
