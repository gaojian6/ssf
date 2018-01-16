/*
 * @date 2016年11月10日 15:20
 */
package com.icourt.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.icourt.common.ConvertUtil;
import com.icourt.common.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author june
 */
@EnableSwagger2
@ConditionalOnClass({Docket.class,BeanValidatorPluginsConfiguration.class})
@Import(BeanValidatorPluginsConfiguration.class)
@EnableConfigurationProperties(SwaggerProperties.class)
public class ApplicationSwaggerAutoConfiguration {

    private final Logger log = LoggerFactory.getLogger(ApplicationSwaggerAutoConfiguration.class);

    //includePattern
    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";


    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
    @Bean
    @ConditionalOnClass(name="org.springframework.data.domain.Pageable")
    public PageableParameterBuilderPlugin pageableParameterBuilderPlugin(TypeNameExtractor nameExtractor, TypeResolver resolver){
        return new PageableParameterBuilderPlugin(nameExtractor,resolver);
    }

    /**
     * Swagger Springfox configuration.
     *
     * @param swaggerProperties the properties of the application
     * @return the Swagger Springfox configuration
     */
    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket swaggerSpringfoxDocket(SwaggerProperties swaggerProperties) {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        Contact contact = new Contact(
                swaggerProperties.getContactName(),
                swaggerProperties.getContactUrl(),
                swaggerProperties.getContactEmail());

        ApiInfo apiInfo = new ApiInfo(
                swaggerProperties.getTitle(),
                swaggerProperties.getDescription(),
                swaggerProperties.getVersion(),
                swaggerProperties.getTermsOfServiceUrl(),
                contact,
                swaggerProperties.getLicense(),
                swaggerProperties.getLicenseUrl());

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class);
        ApiSelectorBuilder apiSelectorBuilder = docket.select();
        if(StrKit.isNotEmpty(swaggerProperties.getIncludePattern())){
            docket = apiSelectorBuilder
                .paths(regex(swaggerProperties.getIncludePattern()))
                    .build();
        }else if(StrKit.isNotEmpty(swaggerProperties.getBasePackage())){
            ConvertUtil.convertStringToList(swaggerProperties.getBasePackage())
                .stream()
                .forEach(basePackage -> apiSelectorBuilder
                        .apis(RequestHandlerSelectors.basePackage(basePackage)));
            docket = apiSelectorBuilder.build();
        }else{
            docket = apiSelectorBuilder
//                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                    .paths(Predicates.not(regex("/error.*")))
                    .paths(Predicates.not(regex("/ssfError.*")))
                    .build();
        }
        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }
}
