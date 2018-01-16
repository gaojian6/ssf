/*
 * @date 2016年11月21日 10:55
 */
package com.icourt.core.xss;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * xss过滤配置
 * @author june
 */
public class XssConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE+1)
    public FilterRegistrationBean xssFilterRegistration(XssCommonsMultipartResolver resolver) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new XssFilter(resolver));
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public XssCommonsMultipartResolver xssCommonsMultipartResolver(){
        return new XssCommonsMultipartResolver();
    }

    @Bean
    public XssHandlerMappingPostProcessor xssHandlerMappingPostProcessor(){
        return new XssHandlerMappingPostProcessor();
    }

}
