/*
 * @date 2017年03月30日 下午8:10
 */
package com.samples;

import com.icourt.core.ApplicationBanner;
import com.icourt.core.log.EnableJsonLogger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication
@EnableJsonLogger
/**
 * @author gaojian
 */
public class SamplesApp extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SamplesApp.class)
                .banner(new ApplicationBanner())
                .run(args);
    }



//    @Bean
//    public JwtFilter jwtFilter(TokenProvider tokenProvider, JwtProperties jwtProperties){
//        return new JwtFilter(tokenProvider,jwtProperties);
//    }


}
