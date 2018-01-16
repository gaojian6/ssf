/*
 * @date 2016年10月28日 10:20
 */
package com.icourt.orm.dao;

import org.springframework.core.Ordered;

/**
 * sql解释器
 * @author june
 */
public interface Interpreter extends Ordered {

    /**
     * 解释
     * @param runtime 当前sql相关信息
     */
    void interpret(StatementRuntime runtime);
}
