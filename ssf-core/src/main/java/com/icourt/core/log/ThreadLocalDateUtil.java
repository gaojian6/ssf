/*
 * @date 2017年10月26日 下午5:48
 */
package com.icourt.core.log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 线程安全的日期格式化
 * @author june
 */
class ThreadLocalDateUtil {

    private ThreadLocalDateUtil(){}

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<>();

    private static DateFormat getDateFormat(){
        DateFormat df = threadLocal.get();
        if(df == null){
            df = new SimpleDateFormat(DATE_FORMAT);
            threadLocal.set(df);
        }
        return df;
    }

    public static String format(Date date){
        return getDateFormat().format(date);
    }

    public static Date parse(String strDate) {
        try {
            return getDateFormat().parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static void remove(){
        threadLocal.remove();
    }

}
