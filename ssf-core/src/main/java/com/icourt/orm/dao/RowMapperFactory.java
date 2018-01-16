/*
 * @date 2016年10月29日 16:30
 */
package com.icourt.orm.dao;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author june
 */
public class RowMapperFactory {

    /**
     * 获取当前要使用的RowMapper
     * @param runtime sql相关
     * @return RowMapper
     */
    public RowMapper getRowMapper(StatementRuntime runtime){
        //当前pojo
        Class pojoClass = runtime.getPojoClass();
        //返回类型
        Class returnType = runtime.getReturnType();
        //todo 根据returnType和pojoClass 使用不同的RowMapper
//        BeanPropertyRowMapper beanPropertyRowMapper = new BeanPropertyRowMapper(pojoClass);
        return new ColumnMapRowMapper();
    }
}
