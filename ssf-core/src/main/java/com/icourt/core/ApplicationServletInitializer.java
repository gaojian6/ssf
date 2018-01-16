/*
 * @date 2016年11月07日 10:32
 */
package com.icourt.core;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 使用外部tomcat时，需要继承该类进行初始化
 * @author june
 */
public abstract class ApplicationServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.banner(new ApplicationBanner());
        application.sources(getAppClass());
        return application;
    }

    protected abstract Class<?> getAppClass();

}
