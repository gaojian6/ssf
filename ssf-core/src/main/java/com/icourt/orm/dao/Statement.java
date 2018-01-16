/*
 * @date 2016年10月28日 17:43
 */
package com.icourt.orm.dao;

/**
 * 封装一个对数据库的操作
 * @author june
 */
public interface Statement {

    /**
     * 执行操作
     * @param statementRuntime sql相关信息
     * @return 结果
     */
    <T> T execute(StatementRuntime statementRuntime);
}
