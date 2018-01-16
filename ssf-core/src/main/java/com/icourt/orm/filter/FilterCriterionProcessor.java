/*
 * @date 2016年12月26日 17:07
 */
package com.icourt.orm.filter;

import org.springframework.core.Ordered;

/**
 * @author june
 */
public interface FilterCriterionProcessor extends Ordered {

    void doProcess(FilterCriterion criterion);

}
