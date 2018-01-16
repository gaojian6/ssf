/*
 * @date 2016年10月28日 18:07
 */
package com.icourt.orm.dao;

import org.springframework.core.annotation.Order;

import java.util.Comparator;

/**
 * 解释器排序
 * @author june
 */
public class InterpreterComparator implements Comparator<Interpreter> {

    @Override
    public int compare(Interpreter thees, Interpreter that) {
        Order thessOrder = thees.getClass().getAnnotation(Order.class);
        Order thatOrder = that.getClass().getAnnotation(Order.class);
        int thessValue = thessOrder == null ? 0 : thessOrder.value();
        int thatValue = thatOrder == null ? 0 : thatOrder.value();
        return thessValue - thatValue;
    }

}
