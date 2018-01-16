/*
 * @date 2016年11月23日 15:45
 */
package com.icourt.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 相关配置
 * @author june
 */
@ConfigurationProperties(prefix="ssf")
public class ApplicationProperties {

    //出错地址
    private String errorPath = "/ssfError";

    /**
     * When to include a "stacktrace" attribute.
     * 是否返回出错堆栈信息
     */
    private IncludeStacktrace includeStacktrace = IncludeStacktrace.NEVER;

    public IncludeStacktrace getIncludeStacktrace() {
        return this.includeStacktrace;
    }

    public void setIncludeStacktrace(IncludeStacktrace includeStacktrace) {
        this.includeStacktrace = includeStacktrace;
    }

    /**
     * Include Stacktrace attribute options.
     */
    public enum IncludeStacktrace {

        /**
         * Never add stacktrace information.
         */
        NEVER,

        /**
         * Always add stacktrace information.
         */
        ALWAYS,

        /**
         * Add stacktrace information when the "trace" request parameter is "true".
         */
        ON_TRACE_PARAM

    }


    public String getErrorPath() {
        return errorPath;
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }
}
