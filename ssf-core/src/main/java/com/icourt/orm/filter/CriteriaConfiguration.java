/*
 * @date 2016年12月26日 20:36
 */
package com.icourt.orm.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/**
 * 检索参数绑定
 * @author june
 */
public class CriteriaConfiguration {

    @Bean
    public FilterCriterionParser filterCriterionParser(){
        return new AdvanceFilterCriterionParser();
    }

    @Bean
    public CriteriaParser criteriaParser(FilterCriterionParser filterCriterionParser){
        return new CriteriaParser(filterCriterionParser);
    }

    @Bean
    public FilterCriterionProcessor filterCriterionProcessor(){
        return new DefaultFilterCriterionProcessor();
    }

    @Bean
    public HandlerMethodArgumentResolver criteriaArgumentResolver(CriteriaParser criteriaParser){
        return new CriteriaArgumentResolver(criteriaParser);
    }

}
