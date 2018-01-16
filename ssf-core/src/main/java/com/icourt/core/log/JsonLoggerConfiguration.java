/*
 * @date 2017年08月11日 下午8:25
 */
package com.icourt.core.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author june
 */
public class JsonLoggerConfiguration {

    @Bean
    @ConditionalOnMissingBean(ILogger.class)
    public ILogger ssfLogger(){
        return new DefaultLogger();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public FilterRegistrationBean loggingFilterRegistration(ILogger logger) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoggingFilter(logger));
        registration.addUrlPatterns("/*");
        return registration;
    }

}
