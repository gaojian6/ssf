/*
 * @date 2016年12月26日 21:52
 */
package com.icourt.orm;


import com.icourt.common.StrKit;
import com.icourt.orm.filter.Criteria;
import com.icourt.orm.filter.Criterion;
import com.icourt.orm.filter.FilterCriterion;
import com.icourt.orm.filter.FilterOperator;
import com.icourt.orm.filter.Junction;
import com.icourt.orm.filter.Or;
import com.icourt.orm.filter.Order;

import java.util.Collection;
import java.util.Map;

/**
 * @author june
 */
public abstract class AbstractDao {

    /**
     * @param criteria         要解析的目标Criteria对象
     * @param useParameterName 在接装查询条件时是否采用参数名
     * @param alias            别名字符串
     * @return ParseResult对象，其中包含解析生成SQL拼装对象以及查询条件的值对象Map,其中key为查询字段名,value为具体条件值
     */
    protected ParseResult parseCriteria(Criteria criteria, boolean useParameterName, String alias) {
        int parameterNameCount = 0;
        if (criteria == null || criteria.getCriterions().size() == 0) {
            return null;
        }
        ParseResult result = new ParseResult();
        StringBuffer sb = result.getAssemblySql();
        Map<String, Object> valueMap = result.getValueMap();
        int count = 0;
        for (Criterion c : criteria.getCriterions()) {
            if (count > 0) {
                sb.append(" and ");
            }
            count++;
            parameterNameCount = buildCriterion(sb, c, valueMap, parameterNameCount, useParameterName, alias);
        }
        return result;
    }

    private int buildCriterion(StringBuffer sb, Criterion c, Map<String, Object> valueMap, int parameterNameCount, boolean useParameterName, String alias) {
        if (c instanceof FilterCriterion) {
            parameterNameCount++;
            FilterCriterion fc = (FilterCriterion) c;
            String operator = buildOperator(fc.getFilterOperator());
            String propertyName = buildFieldName(fc.getProperty());
            if (StrKit.isNotEmpty(alias) && !propertyName.contains(".")) {
                sb.append(" ").append(alias).append(".`").append(propertyName).append("` ");
            } else if(propertyName.contains(".")){
                sb.append(" ").append(propertyName).append(" ");
            }else{
                sb.append(" `").append(propertyName).append("` ");
            }
            sb.append(" ").append(processLike(operator)).append(" ");
            String paramName = fc.getProperty();
            if (paramName.contains(".")) {
                paramName = paramName.replaceAll("\\.", "_");
            }
            String prepareName = paramName + "_" + parameterNameCount + "_";
            if (useParameterName) {
                sb.append(" :").append(prepareName).append(" ");
            } else {
                sb.append(" ? ");
            }
            if (operator.equals("like")) {
                valueMap.put(prepareName, "%" + fc.getValue() + "%");
            } else if (operator.startsWith("*")) {
                valueMap.put(prepareName, "%" + fc.getValue());
            } else if (operator.endsWith("*")) {
                valueMap.put(prepareName, fc.getValue() + "%");
            } else {
                valueMap.put(prepareName, fc.getValue());
            }
        }
        if (c instanceof Junction) {
            Junction jun = (Junction) c;
            String junction = " and ";
            if (jun instanceof Or) {
                junction = " or ";
            }
            int count = 0;
            Collection<Criterion> criterions = jun.getCriterions();
            if (criterions != null) {
                sb.append(" ( ");
                for (Criterion criterion : criterions) {
                    if (count > 0) {
                        sb.append(junction);
                    }
                    count++;
                    parameterNameCount = this.buildCriterion(sb, criterion, valueMap, parameterNameCount, useParameterName, alias);
                }
                sb.append(" ) ");
            }
        }
        return parameterNameCount;
    }

    private String buildOperator(FilterOperator filterOperator) {
        String operator = "like";
        if (filterOperator != null) {
            operator = filterOperator.toString();
        }
        return operator;
    }

    private String processLike(String operator) {
        String result = operator;
        if (operator.endsWith("*")) {
            result = operator.substring(0, operator.length() - 1);
        }
        if (operator.startsWith("*")) {
            result = operator.substring(1, operator.length());
        }
        return result;
    }

    /**
     * 子类可以覆盖该方法，以决定该如何根据给出的字段名构建拼装查询条件的字段名
     *
     * @param name 需要重新构建的查询字段名
     * @return 返回构建好的字段名
     */
    protected String buildFieldName(String name) {
        return name;
    }

    /**
     * 替换属性名称
     * @param propertyMap 旧属性和新属性的映射关系
     * @param criteria 检索参数对象
     */
    protected void replaceProperty(Criteria criteria, Map<String, String> propertyMap) {
        if (criteria == null || (criteria.getCriterions().size() == 0 && criteria.getOrders().size() == 0) || propertyMap == null || propertyMap.isEmpty()) {
            return;
        }
        for (Criterion criterion : criteria.getCriterions()) {
            if (criterion instanceof FilterCriterion) {
                FilterCriterion fc = (FilterCriterion) criterion;
                String propertyName = fc.getProperty();
                String newPropertyName = propertyMap.get(propertyName);
                if (StrKit.isNotEmpty(newPropertyName)) {
                    fc.setProperty(newPropertyName);
                }
            }
        }
        for (Order order : criteria.getOrders()) {
            String property = order.getProperty();
            String newProperty = propertyMap.get(property);
            if (StrKit.isNotEmpty(newProperty)) {
                order.setProperty(newProperty);
            }
        }
    }

    /**
     * 获取排序sql
     * @param criteria 查询参数
     * @param alias 别名，不使用别名传null
     * @return 排序sql
     */
    protected String buildOrderSql(Criteria criteria,String alias){
        if(criteria==null || criteria.getOrders().size()==0){
            return "";
        }
        StringBuilder orderSb = new StringBuilder();
        int num = 0;
        orderSb.append(" order by ");
        for(Order order : criteria.getOrders()){
            if(num>0){
                orderSb.append(" , ");
            }
            String property = order.getProperty();
            String tag = order.isDesc() ? "desc" : "asc";
            if(!property.contains(".") && StrKit.isNotEmpty(alias)){
                orderSb.append(" ").append(alias).append(".`").append(property).append("` ").append(tag).append(" ");
            }else{
                orderSb.append(" `").append(property).append("` ").append(tag).append(" ");
            }
            num++;
        }
        return orderSb.toString();
    }



}
