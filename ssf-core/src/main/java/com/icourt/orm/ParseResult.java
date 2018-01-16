/*
 * @date 2016年12月26日 21:43
 */
package com.icourt.orm;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author june
 */
public class ParseResult {

    private Map<String,Object> valueMap = new LinkedHashMap<>();
    private StringBuffer assemblySql = new StringBuffer();

    public Map<String, Object> getValueMap() {
        return valueMap;
    }
    public void setValueMap(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }
    public StringBuffer getAssemblySql() {
        return assemblySql;
    }
    public void setAssemblySql(StringBuffer assemblySql) {
        this.assemblySql = assemblySql;
    }
}
