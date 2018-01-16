/*
 * @date 2017年01月04日 16:00
 */
package com.icourt.core;

import com.icourt.common.ClassKit;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 日期参数转换
 * @author june
 */
public class DateConverter implements Converter<String,Date> {

    @Override
    public Date convert(String source) {
        return (Date) ClassKit.getObjectValue(source,Date.class.getName());
    }
}
