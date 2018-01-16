/*
 * @date 2016年12月26日 16:50
 */
package com.icourt.orm.filter;

/**
 * @author june
 */
public class Order {

    private String property;
    private boolean desc;

    public Order(String property, boolean desc) {
        this.property = property;
        this.desc = desc;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }
}
