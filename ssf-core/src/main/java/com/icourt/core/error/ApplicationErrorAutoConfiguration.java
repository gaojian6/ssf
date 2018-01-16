/*
 * @date 2016年11月23日 16:18
 */
package com.icourt.core.error;

import com.icourt.core.ApplicationProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;

/**
 * @author june
 */
@ConditionalOnWebApplication
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@AutoConfigureAfter(ErrorMvcAutoConfiguration.class)
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class ApplicationErrorAutoConfiguration {

    private final ApplicationProperties applicationProperties;
    private final ApplicationContext applicationContext;

    private final ResourceProperties resourceProperties;

    public ApplicationErrorAutoConfiguration(ApplicationProperties applicationProperties, ApplicationContext applicationContext, ResourceProperties resourceProperties) {
        this.applicationProperties = applicationProperties;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
    }

    @Bean("ssfErrorPageCustomizer")
    public ErrorPageCustomizer errorPageCustomizer() {
        return new ErrorPageCustomizer(this.applicationProperties);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(ApplicationErrorController.class)
    public ApplicationErrorController seasonErrorController(ErrorAttributes errorAttributes){
        return new ApplicationErrorController(errorAttributes, this.applicationProperties,applicationContext,resourceProperties);
    }

    @Bean
    @ConditionalOnMissingBean(ExceptionTranslator.class)
    public ExceptionTranslator exceptionTranslator(){
        return new ExceptionTranslator();
    }


    /**
     * {@link EmbeddedServletContainerCustomizer} that configures the container's error
     * pages.
     */
    private static class ErrorPageCustomizer implements ErrorPageRegistrar, Ordered {

        private final ApplicationProperties properties;

        protected ErrorPageCustomizer(ApplicationProperties properties) {
            this.properties = properties;
        }

        @Override
        public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
            ErrorPage errorPage = new ErrorPage(this.properties.getErrorPath());
            errorPageRegistry.addErrorPages(errorPage);
        }

        @Override
        public int getOrder() {
            return 0;
        }

    }

}
