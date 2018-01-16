/*
 * @date 2016年12月26日 18:14
 */
package com.icourt.orm.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icourt.common.StrKit;

import java.util.Map;

/**
 * @author june
 */
public class CriteriaParser {

    private final FilterCriterionParser filterCriterionParser;

    public CriteriaParser(FilterCriterionParser filterCriterionParser) {
        this.filterCriterionParser = filterCriterionParser;
    }

    /**
     * 解析json串
     * @param criteriaJson json
     * @return Criteria
     */
    public Criteria parse(String criteriaJson){

        JSONObject jsonObject = JSON.parseObject(criteriaJson);
        Criteria criteria = new Criteria();
        if (jsonObject.containsKey("criterions")) {
            JSONArray criterionsJsonArray =  jsonObject.getJSONArray("criterions");
            if (criterionsJsonArray != null) {
                for (int i = 0; i < criterionsJsonArray.size(); i++) {
                    JSONObject criterionJsonObj = criterionsJsonArray.getJSONObject(i);
                    criteria.addCriterion(parseCriterion(criterionJsonObj));
                }
            }
        }
        if (jsonObject.containsKey("orders")) {
            JSONArray ordersJsonArray = jsonObject.getJSONArray("orders");
            if (ordersJsonArray != null) {
                for (int i = 0; i < ordersJsonArray.size(); i++) {
                    JSONObject orderJsonObj = ordersJsonArray.getJSONObject(i);
                    Order order = new Order(orderJsonObj.getString("property"),
                            orderJsonObj.getBoolean("desc"));
                    criteria.addOrder(order);
                }
            }
        }
        return criteria;
    }

    private Criterion parseCriterion(JSONObject criterionJsonObj) {
        String junction = criterionJsonObj.getString("junction");
        if (StrKit.isNotEmpty(junction)) {
            Junction junctionCrition;
            if ("or".equals(junction)) {
                junctionCrition = new Or();
            } else {
                junctionCrition = new And();
            }
            JSONArray criterionsJsonArray =  criterionJsonObj.getJSONArray("criterions");
            if (criterionsJsonArray != null) {
                for (int i = 0; i < criterionsJsonArray.size(); i++) {
                    JSONObject junctionCriterionJsonObj = criterionsJsonArray.getJSONObject(i);
                    junctionCrition.addCriterion(parseCriterion(junctionCriterionJsonObj));
                }
            }
            return junctionCrition;
        } else {
            String property = criterionJsonObj.getString("property");
            String expression = criterionJsonObj.getString("expression");
            String dataTypeName = criterionJsonObj.getString("dataType");
            DataType dataType = null;
            if (StrKit.isNotEmpty(dataTypeName)) {
                dataType = getDataType(dataTypeName);
            }
            return filterCriterionParser.createFilterCriterion(property,
                    dataType, expression);
        }

    }

    /**
     * 根据字符串名称获取DataType
     * @param dataTypeName 名称
     * @return DataType
     */
    private DataType getDataType(String dataTypeName){
        if(dataTypeName.equals("Date")){
            return DataType.Date;
        }
        if(dataTypeName.equals("Boolean")){
            return DataType.Boolean;
        }
        if(dataTypeName.equals("Integer")){
            return DataType.Integer;
        }
        if(dataTypeName.equals("Long")){
            return DataType.Long;
        }
        if(dataTypeName.equals("Double")){
            return DataType.Double;
        }
        return DataType.String;
    }

    /**
     * 根据map构建检索参数
     * @param propertyMap 属性和属性值的map 必须都是String类型的 进行like查询的
     * @return Criteria
     */
    public Criteria buildLikeCriteria(Map<String,String> propertyMap){
        if(propertyMap == null || propertyMap.isEmpty()){
            return null;
        }
        Criteria criteria = new Criteria();
        String[] keys = propertyMap.keySet().toArray(new String[0]);
        for (String key : keys) {
            criteria.addCriterion(filterCriterionParser.createFilterCriterion(key,
                    DataType.String, "*"+propertyMap.get(key)+"*"));
        }
        return criteria;
    }
}
