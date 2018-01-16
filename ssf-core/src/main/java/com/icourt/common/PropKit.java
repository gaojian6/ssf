package com.icourt.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 配置文件相关工具类
 * @author june
 */
public class PropKit {

    private static Logger logger = LoggerFactory.getLogger(PropKit.class);
    /**
     * 替换字符串中的配置信息
     *
     * @param propKey ${adminPath}/hello/test
     * @return /bbs/hello/test
     */
    public static String replaceProp(String propKey) {
        return replaceProp(propKey,propKey);
    }

    /**
     * 替换字符串中的配置信息
     * @param propKey ${adminPath}/hello/test
     * @param defaultValue 默认值
     * @return /bbs/hello/test
     */
    public static String replaceProp(String propKey, String defaultValue) {
        if(BeanKit.getApplicationContext()==null){
            logger.warn("ApplicationContext is null");
            return defaultValue;
        }
        Pattern p = Pattern.compile("\\$\\{(.*?)}");
        Matcher m = p.matcher(propKey);
        StringBuffer sb = new StringBuffer();
        boolean flg = false;
        while (m.find()) {
            flg = true;
            String key = m.group(1);
            if(StrKit.isNotEmpty(key)){
                Environment environment = BeanKit.getBean(Environment.class);
                String value = environment.getProperty(key,defaultValue);
                value = value==null?"":value;
//                String value = "123";
                m.appendReplacement(sb, value);
            }
        }
        //没有找到则直接返回
        if(!flg){
            return propKey;
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
