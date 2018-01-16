/*
 * @date 2016年12月26日 17:05
 */
package com.icourt.orm.filter;

import com.icourt.common.StrKit;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author june
 */
public class AdvanceFilterCriterionParser implements FilterCriterionParser,ApplicationContextAware {

    private List<FilterCriterionProcessor> criterionProcessors;

    @Override
    public Criterion createFilterCriterion(String property, DataType dataType, String expression) {
        if(StrKit.isEmpty(expression)){
            return null;
        }
        FilterCriterion filterCriterion = new FilterCriterion();
        filterCriterion.setDataType(dataType);
        filterCriterion.setExpression(expression);
        filterCriterion.setProperty(property);
        for (FilterCriterionProcessor processor : criterionProcessors) {
            processor.doProcess(filterCriterion);
            if (filterCriterion.getFilterOperator() != null) {
                return filterCriterion;
            }
        }
        throw new IllegalArgumentException("Unsupported expression ["
                + expression + "]");
    }

    public List<FilterCriterionProcessor> getCriterionProcessors() {
        return criterionProcessors;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, FilterCriterionProcessor> map = applicationContext.getBeansOfType(FilterCriterionProcessor.class);
        criterionProcessors = new ArrayList<>(map.values());
        //排序
        criterionProcessors.sort(Comparator.comparingInt(Ordered::getOrder));
    }
}
