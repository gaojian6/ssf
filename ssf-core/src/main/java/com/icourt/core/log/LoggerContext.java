/*
 * @date 2017年08月11日 下午5:03
 */
package com.icourt.core.log;

/**
 * 日志信息放入线程
 * @author june
 */
public class LoggerContext {

    private final static ThreadLocal<ILogInfo> logInfoThreadLocal = new ThreadLocal<>();

    public static void setLogInfo(ILogInfo logInfo) {
        logInfoThreadLocal.set(logInfo);
    }

    public static ILogInfo getLogInfo() {
        return logInfoThreadLocal.get();
    }

    public static void remove(){
        logInfoThreadLocal.remove();
    }
}
