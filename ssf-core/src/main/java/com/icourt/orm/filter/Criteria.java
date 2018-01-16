/*
 * @date 2016年12月26日 16:48
 */
package com.icourt.orm.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台传入json示例
 * {
     "criteria": {
         "criterions": [
             {
                 "junction": "or",
                 "criterions": [
                     {
                         "property": "name",
                         "dataType": "String",
                         "expression": "*水*"
                     },
                     {
                         "property": "name",
                         "dataType": "String",
                         "expression": "*吃*"
                     },
                     {
                         "property": "name",
                         "dataType": "String",
                         "expression": "睡觉*"
                     }
                 ]
             },
         {
             "property": "money",
             "dataType": "Double",
             "expression": ">100"
         },
         {
             "property": "money",
             "dataType": "Double",
             "expression": "<200"
         },
         {
             "property": "doDate",
             "dataType": "Date",
             "expression": ">=2016-12-19"
         },
         {
             "property": "doDate",
             "dataType": "Date",
             "expression": "<=2016-12-26"
         }
         ],
         "orders": [
             {
                 "property": "type",
                 "desc": false
             }
         ]
    }
 }
 * @author june
 */
public class Criteria {

    private List<Criterion> criterions = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    public void addCriterion(Criterion criterion) {
        criterions.add(criterion);
    }

    public List<Criterion> getCriterions() {
        return criterions;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
