/*
 * @date 2016年12月26日 17:19
 */
package com.icourt.orm.filter;

import com.icourt.common.ClassKit;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.core.Ordered;

import java.util.Date;

/**
 * @author june
 */
public class DefaultFilterCriterionProcessor implements FilterCriterionProcessor {

    private static final FilterOperator[] OPERATORS = new FilterOperator[] {
            FilterOperator.like, FilterOperator.likeStart,
            FilterOperator.likeEnd, FilterOperator.eq, FilterOperator.gt,
            FilterOperator.ge, FilterOperator.lt, FilterOperator.le,
            FilterOperator.ne };


    @Override
    public void doProcess(FilterCriterion criterion) {
        String expression = criterion.getExpression();
        DataType dataType = criterion.getDataType();
        FilterOperator filterOperator = null;

        for (int i = OPERATORS.length - 1; i >= 0; i--) {
            FilterOperator operator = OPERATORS[i];
            if (expression.startsWith(operator.toString())) {
                filterOperator = operator;
                expression = expression.substring(operator.toString().length());
                break;
            }
        }

        expression = expression.trim();
        if (filterOperator == null) {
            int len = expression.length();
            if ((dataType == null || dataType == DataType.String)
                    && len > 1) {
                char firstChar = expression.charAt(0), lastChar = expression
                        .charAt(len - 1);
                if (len > 2 && expression.charAt(len - 2) == '\\') {
                    lastChar = 0;
                }

                if (firstChar != '*' && firstChar != '%') {
                    firstChar = 0;
                }
                if (lastChar != '*' && lastChar != '%') {
                    lastChar = 0;
                }

                if (firstChar > 0) {
                    if (lastChar > 0) {
                        if (len > 2) {
                            filterOperator = FilterOperator.like;
                            expression = expression.substring(1, len - 1);
                        } else {
                            filterOperator = FilterOperator.eq;
                        }
                    } else {
                        filterOperator = FilterOperator.likeEnd;
                        expression = expression.substring(1);
                    }
                } else if (lastChar > 0) {
                    filterOperator = FilterOperator.likeStart;
                    expression = expression.substring(0, len - 1);
                } else {
                    filterOperator = FilterOperator.like;
                }
            } else {
                filterOperator = FilterOperator.like;
            }
        }

        if (expression.indexOf('\\') >= 0) {
            expression = StringEscapeUtils.escapeJava(expression);
        }
        Object value = (dataType != null) ? fromText(expression,dataType)
                : expression;
        criterion.setFilterOperator(filterOperator);
        criterion.setValue(value);
    }

    /**
     * 尝试转换成相应类型的数据
     * @param text 文本
     * @param dataType 类型
     * @return value
     */
    private Object fromText(String text,DataType dataType){
        String type = String.class.getName();
        if(dataType == DataType.Date){
            type = Date.class.getName();
        }
        if(dataType == DataType.Boolean){
            type = Boolean.class.getName();
        }
        if(dataType == DataType.Double){
            type = Double.class.getName();
        }
        if(dataType == DataType.Long){
            type = Long.class.getName();
        }
        if(dataType == DataType.Integer){
            type = Integer.class.getName();
        }
        return ClassKit.getObjectValue(text,type);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
