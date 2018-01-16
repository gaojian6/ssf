/*
 * @date 2016年10月28日 18:09
 */
package com.icourt.orm.dao;

import com.icourt.orm.datasource.DataSourceRegister;
import org.springframework.core.Ordered;

/**
 * 默认解释器
 * @author june
 */
public class SystemInterpreter implements Interpreter{

    @Override
    public void interpret(StatementRuntime statementRuntime) {
        String dsName = statementRuntime.getDsName();
        boolean readonly= DataSourceRegister.readOnlyMap.get(dsName);
        if(statementRuntime.getSqlType().equals(SQLType.WRITE) && readonly){
            throw new ApplicationDaoException("该数据源只读：dsName:"+dsName);
        }

        //打印sql
//        Map<String,Object> paramMap = statementRuntime.getParamMap();
//        if(paramMap!=null){
//            SqlReporter.doReport(statementRuntime.getSql(),paramMap);
//        }else{
//            SqlReporter.doReport(statementRuntime.getSql(),statementRuntime.getParams());
//        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
