/*
 * @date 2017年08月11日 下午5:04
 */
package com.icourt.core.log;

/**
 * 默认记录日志字段信息
 * @author june
 */
public class DefaultLogInfo implements ILogInfo{

    private String logId;//一个线程的唯一主键
    private String level;//日志级别
    private String threadInfo;//线程信息，线程名称+线程id
    private String message;//日志内容
    private String className;//调用的类名称
    private String methodName;//方法名
    private String lineNumber;//行号

    private String serverInfo;//ip+contextPath

    private String requestURI;//请求uri
    private String requestMethod;//请求方式
    private String requestParams;//参数，get时
    private String requestIp;//客户端ip
    private String userAgent;//头信息

    private String loggingTime;//日志记录时间

    private String startTime;//请求开始时间
    private long time;//耗时

    private String exception;//异常信息


    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getThreadInfo() {
        return threadInfo;
    }

    public void setThreadInfo(String threadInfo) {
        this.threadInfo = threadInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLoggingTime() {
        return loggingTime;
    }

    public void setLoggingTime(String loggingTime) {
        this.loggingTime = loggingTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getException() {
        return exception;
    }

    @Override
    public void setException(String exception) {
        this.exception = exception;
    }
}
