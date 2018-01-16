/*
 * @date 2016年12月26日 17:04
 */
package com.icourt.orm.filter;

/**
 * @author june
 */
public interface FilterCriterionParser {

    Criterion createFilterCriterion(String property, DataType dataType,
                                    String expression);

}
