/*
 * @date 2017年08月11日 下午7:22
 */
package com.icourt.core.log;

import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.icourt.common.ExceptionKit;
import com.icourt.common.IpKit;
import com.icourt.core.json.JsonKit;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * @author june
 */
public class DefaultLogger implements ILogger{

    protected final static String localIp = IpKit.getLocalIp();

    @Override
    public void start(HttpServletRequest request) {
        //放入当前线程
        LoggerContext.setLogInfo(buildDefaultLogInfo(request));
    }

    /**
     * 构建DefaultLogInfo
     * @param request  request
     * @return DefaultLogInfo
     */
    protected DefaultLogInfo buildDefaultLogInfo(HttpServletRequest request){
        DefaultLogInfo logInfo = new DefaultLogInfo();
        logInfo.setLogId(UUID.randomUUID().toString());
        logInfo.setThreadInfo(getThreadInfo());

        logInfo.setRequestURI(request.getRequestURI());
        logInfo.setRequestIp(IpKit.getIpAddr(request));
        logInfo.setRequestMethod(request.getMethod());
        logInfo.setRequestParams(JsonKit.toJSONString(request.getParameterMap()));
        logInfo.setUserAgent(request.getHeader("User-Agent"));

        logInfo.setServerInfo(getServerInfo(request));
        logInfo.setStartTime(ThreadLocalDateUtil.format(new Date()));
        return logInfo;
    }

    @Override
    public void logging(ILoggingEvent event) {

        DefaultLogInfo logInfo = (DefaultLogInfo) LoggerContext.getLogInfo();
        if(logInfo == null){
            logInfo = new DefaultLogInfo();
        }
        logInfo = setLoggingEventInfo(logInfo,event);

        LoggerContext.remove();
        //放回当前线程
        LoggerContext.setLogInfo(logInfo);
    }

    /**
     * 添加log本身的信息
     * @param logInfo 日志
     * @param event event
     * @return 日志信息
     */
    protected DefaultLogInfo setLoggingEventInfo(DefaultLogInfo logInfo,ILoggingEvent event){
        if(logInfo == null){
            logInfo = new DefaultLogInfo();
        }

        logInfo.setClassName(getFullyQualifiedName(event));
        logInfo.setLevel(event.getLevel().toString());
        logInfo.setLineNumber(getLineNumber(event));
        logInfo.setMethodName(getMethodName(event));
        logInfo.setLoggingTime(ThreadLocalDateUtil.format(new Date(event.getTimeStamp())));
        logInfo.setMessage(event.getFormattedMessage());
        if(logInfo.getStartTime() == null){
            logInfo.setStartTime(logInfo.getLoggingTime());
        }
        Date startTime = ThreadLocalDateUtil.parse(logInfo.getStartTime());
        if(startTime != null) {
            //记录耗时
            logInfo.setTime(System.currentTimeMillis() - startTime.getTime());
        }
        return logInfo;
    }




    /**
     * 获取当前线程信息
     *
     * @return 线程信息
     */
    protected String getThreadInfo() {
        Thread thread = Thread.currentThread();
        return "[" + thread.getId() + "]" + thread.getName();
    }

    /**
     * 获取系统信息
     * @param request request
     * @return serverInfo
     */
    protected String getServerInfo(HttpServletRequest request) {
        return localIp+request.getContextPath();
    }

    /**
     * 类名
     * @param le event
     * @return className
     */
    protected String getFullyQualifiedName(ILoggingEvent le) {
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > 0) {
            return cda[0].getClassName();
        } else {
            return CallerData.NA;
        }
    }

    /**
     * 行号
     * @param le event
     * @return lineNumber
     */
    protected String getLineNumber(ILoggingEvent le) {
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > 0) {
            return Integer.toString(cda[0].getLineNumber());
        } else {
            return CallerData.NA;
        }
    }

    /**
     * 方法名
     * @param le event
     * @return MethodName
     */
    protected String getMethodName(ILoggingEvent le) {
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > 0) {
            return cda[0].getMethodName();
        } else {
            return CallerData.NA;
        }
    }

    /**
     * 处理异常堆栈
     * @param _e 异常
     * @return 堆栈信息
     */
    protected String makeException(Exception _e) {
        if (_e == null) {
            return "";
        }
        String sResult = ExceptionKit.getStackTraceText(_e);
        sResult = sResult.replaceAll("\n", "\\\\n");
        sResult = sResult.replaceAll("\r", "\\\\r");
        sResult = sResult.replaceAll("\t", "\\\\t");
        return sResult;
    }
}
