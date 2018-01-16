package com.icourt.orm.dao;

import java.io.Serializable;
import java.util.Map;

/**
 * 解析后的sql
 * @author june
 */
public class SqlParams implements Serializable {

    //sql
    private String sql;

    //参数,sql中的？
    private Object[] params;

    //参数化的值
    private Map<String,Object> paramMap;

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SqlParams(){}

    public SqlParams(String sql) {
        this.sql = sql;
    }

    public SqlParams(Object[] params, String sql) {
        this.params = params;
        this.sql = sql;
    }

    public SqlParams(Map<String, Object> paramMap,String sql) {
        this.sql = sql;
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

}
