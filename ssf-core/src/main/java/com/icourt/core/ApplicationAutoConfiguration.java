/*
 * @date 2016年10月03日 10:44
 */
package com.icourt.core;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.icourt.common.BeanKit;
import com.icourt.core.captcha.CaptchaHelper;
import com.icourt.core.json.IJsonService;
import com.icourt.core.json.JsonService;
import com.icourt.core.xss.XssConfiguration;
import com.icourt.data.CacheFilter;
import com.icourt.data.SpringCacheKeyGenerator;
import com.icourt.orm.filter.CriteriaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * spring bean 全部显示声明
 *
 * @author june
 */
@EnableConfigurationProperties(ApplicationProperties.class)
@Import({CriteriaConfiguration.class,XssConfiguration.class})
public class ApplicationAutoConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    @Qualifier("criteriaArgumentResolver")
    private HandlerMethodArgumentResolver criteriaArgumentResolver;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public FilterRegistrationBean cacheFilterRegistration(ApplicationContext applicationContext) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CacheFilter(applicationContext));
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public BeanKit beanKit() {
        return new BeanKit();
    }

    @Bean
    @ConditionalOnMissingBean(AbstractGenericHttpMessageConverter.class)
    public FastJsonHttpMessageConverter4 fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter4 fastConverter = new FastJsonHttpMessageConverter4();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);
        return fastConverter;
    }

    @Bean
    public ApplicationCommandLineRunner startLogCommandLineRunner(Environment env){
        return new StartLogCommandLineRunner(env);
    }

    @Bean
    public CaptchaHelper captchaHelper(){
        return new CaptchaHelper();
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(criteriaArgumentResolver);
    }

    @Bean
    public DateConverter dateConverter(){
        return new DateConverter();
    }

    @Bean
    @ConditionalOnMissingBean(ConversionServiceFactoryBean.class)
    public ConversionServiceFactoryBean conversionServiceFactoryBean(DateConverter dateConverter){
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        Set<Converter> converters = new HashSet<>();
        converters.add(dateConverter);
        factoryBean.setConverters(converters);
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }

    @Bean
    @ConditionalOnMissingBean(IJsonService.class)
    public IJsonService jsonService(){
        return new JsonService();
    }

    @Bean
    public SpringCacheKeyGenerator springCacheKeyGenerator(){
        return new SpringCacheKeyGenerator();
    }
}
