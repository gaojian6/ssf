/*
 * @date 2016年12月20日 15:08
 */
package com.icourt.quartz;

import com.icourt.core.ApplicationCommandLineRunner;
import org.quartz.JobBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;

/**
 * @author june
 */
@ConditionalOnClass({ JobBuilder.class })
@ConditionalOnResource(resources = "classpath:/quartz.properties")
@ConditionalOnBean(IQuartzAuthService.class)
public class ApplicationQuartzAutoConfiguration {

    @Bean
    public JobBeanDao jobBeanDao(){
        return new JobBeanDao();
    }

    @Bean
    @ConditionalOnMissingBean(IQuartzService.class)
    public IQuartzService quartzService(){
        return new QuartzService();
    }

    @Bean
    public QuartzController quartzController(){
        return new QuartzController();
    }

    @Bean
    public ApplicationCommandLineRunner quartzCommandLineRunner(){
        return new QuartzCommandLineRunner();
    }

}
