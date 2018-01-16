/*
 * @date 2017年08月11日 下午4:24
 */
package com.icourt.core.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.icourt.common.BeanKit;
import com.icourt.core.json.JsonKit;

/**
 * @author june
 */
public class JsonConverter extends ClassicConverter {

    private ThrowableProxyConverter tpc = new ThrowableProxyConverter();

    @Override
    public void start() {
        tpc.start();
    }

    @Override
    public String convert(ILoggingEvent event) {

        //spring没初始化不使用该日志功能,只输出基本信息
        if(BeanKit.getApplicationContext() == null){
            return defaultConvert(event);
        }
        ILogger logger = BeanKit.getBean(ILogger.class);
        if(logger == null){
            return defaultConvert(event);
        }
        logger.logging(event);
        ILogInfo logInfo = LoggerContext.getLogInfo();
        if(logInfo == null){
            logInfo = new DefaultLogInfo();
        }
        //处理异常信息
        logInfo.setException(tpc.convert(event));
        return JsonKit.toJSONString(logInfo);
    }


    /**
     * spring 没有初始化时默认输出
     * @param event event
     * @return 日志内容
     */
    protected String defaultConvert(ILoggingEvent event){
        String className = CallerData.NA;
        String methodName = CallerData.NA;
        String lineNumber = CallerData.NA;
        StackTraceElement[] cda = event.getCallerData();
        if (cda != null && cda.length > 0) {
            className = cda[0].getClassName();
            lineNumber = Integer.toString(cda[0].getLineNumber());
            methodName = cda[0].getMethodName();
        }
        String logInfo = "{";
        logInfo += "\"loggingTime\":"+event.getTimeStamp()+",";
        logInfo += "\"message\":"+event.getFormattedMessage()+",";
        logInfo += "\"level\":"+event.getLevel().toString()+",";
        logInfo += "\"className\":"+className+",";
        logInfo += "\"methodName\":"+methodName+",";
        logInfo += "\"lineNumber\":"+lineNumber+",";
        logInfo += "\"exception\":\""+tpc.convert(event)+"\"";
        logInfo += "}";
        return logInfo;
    }
}
