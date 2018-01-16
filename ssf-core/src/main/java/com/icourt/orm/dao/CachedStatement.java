/*
 * @date 2016年10月28日 17:49
 */
package com.icourt.orm.dao;

/**
 * 缓存数据库查询
 * @author june
 */
public class CachedStatement implements Statement{

    /**
     * 实际底层Statement，比如就是数据库操作的实际执行语句
     */
    private final Statement realStatement;

    /**
     * cache服务接口
     */
    private final CacheProvider cacheProvider;

    public CachedStatement(CacheProvider cacheProvider, Statement realStatement) {
        this.realStatement = realStatement;
        this.cacheProvider = cacheProvider;
    }

    @Override
    public <T> T execute(StatementRuntime statementRuntime) {
        return null;
    }
}
