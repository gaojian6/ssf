/*
 * @date 2016年12月19日 08:17
 */
package com.icourt.data;


import com.icourt.common.DateKit;

import java.util.Calendar;
import java.util.Date;

/**
 * @author june
 */
public class CacheTimeKit {

    //默认缓存24h,一般都不建议永久缓存
    public static int defaultTime = 24*60*60;
    //缓存一个月
    public static int OneMonth = 30*24*60*60;
    //缓存3个月
    public static int ThreeMonth = 3*30*24*60*60;
    //缓存6个月
    public static int SixMonth = 6*30*24*60*60;
    //缓存一年
    public static int OneYear = 360*24*60*60;
    /**
     * 获取当天的剩余时间,用来缓存按天的限制
     * @return 当天的剩余时间 秒
     */
    public static int getDayTime(){
        Date currDate = new Date();
        //明天
        Date tomorrow = DateKit.getBeforeDay(currDate,1);
        String tomorrowStr = DateKit.getDateStr(tomorrow);
        tomorrowStr += " 00:00:00";
        Date date = DateKit.getDate("yyyy-MM-dd HH:mm:ss",tomorrowStr);
        assert date != null;
        long time = date.getTime()-currDate.getTime();
        return (int)time/1000;
    }

    /**
     * 获取当月的剩余时间,用来缓存按自然月的限制
     *
     * @return 当月的剩余时间 秒
     */
    public static int getMothTime(){
        Date currDate = new Date();
        //本月最后一天
        Date lastDateOfMonth = DateKit.getLastDateOfMonth(currDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDateOfMonth);
        calendar.set(Calendar.MILLISECOND,calendar.getActualMaximum(Calendar.MILLISECOND));
        calendar.set(Calendar.SECOND,calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MINUTE,calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        long interval = (calendar.getTime().getTime() - currDate.getTime())/1000;
        return (int) interval;
    }

}
