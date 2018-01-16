/*
 * @date 2017年08月11日 下午6:04
 */
package com.icourt.core.log;

import ch.qos.logback.classic.spi.ILoggingEvent;

import javax.servlet.http.HttpServletRequest;

/**
 * 生成日志信息
 * @author june
 */
public interface ILogger {

    /**
     * 请求开始
     * @param request request
     */
    void start(HttpServletRequest request);

    /**
     * 开始记录日志
     * @param event LOG
     */
    void logging(ILoggingEvent event);
}
