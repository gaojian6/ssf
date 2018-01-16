/*
 * @date 2017年08月11日 下午4:35
 */
package com.icourt.core.log;

import ch.qos.logback.classic.PatternLayout;

/**
 * @author june
 */
public class JsonPatternLayout extends PatternLayout {

    static {
        defaultConverterMap.put("json", JsonConverter.class.getName());
    }

}
