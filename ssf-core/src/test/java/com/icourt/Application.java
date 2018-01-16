/*
 * @date 2017年07月24日 下午4:30
 */
package com.icourt;

import com.icourt.core.ApplicationBanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author june
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .banner(new ApplicationBanner())
                .run(args);
    }

}
