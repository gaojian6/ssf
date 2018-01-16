/*
 * @date 2016年10月28日 17:38
 */
package com.icourt.orm.dao;


import com.icourt.data.ICache;

/**
 * 缓存
 * @author june
 */
public interface CacheProvider {

    /**
     * 获取缓存实例
     * @param dsId 当前数据源id
     * @param statementRuntime 要执行的sql
     * @return ICache
     */
    ICache getCache(String dsId, StatementRuntime statementRuntime);

}
