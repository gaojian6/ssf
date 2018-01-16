/*
 * @date 2016年10月29日 11:10
 */
package com.icourt.orm.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 最终执行数据库操作
 * @author june
 */
public interface IDataAccess {

    /**
     * 返回所使用的DataSource
     *
     * @return DataSource
     */
    DataSource getDataSource();

    /**
     * 读访问
     *
     * @param sql 所要执行的实际SQL语句
     * @param args 伴随该SQL语句的参数
     * @param rowMapper 行映射器
     * @return 结果集
     */
    <T> List<T> select(String sql, Object[] args, RowMapper<T> rowMapper);

    /**
     * 写访问（更新或插入）
     *
     * @param sql 所要执行的实际SQL语句
     * @param args 伴随该SQL语句的参数
     * @param generatedKeyHolder 是否要读取该SQL生成的key
     * @return 更新数量
     */
    int update(String sql, Object[] args, KeyHolder generatedKeyHolder);

    /**
     * 批量写访问（更新或插入）
     *
     * @param sql 所要执行的实际SQL语句
     * @param batchArgs 伴随该SQL语句的参数
     * @return 更新数量
     */
    int[] batchUpdate(String sql, List<Object[]> batchArgs);

    /**
     * 读访问,参数名方式
     *
     * @param sql 所要执行的实际SQL语句
     * @param paramMap 伴随该SQL语句的参数
     * @param rowMapper 行映射器
     * @return 结果集
     */
    <T> List<T> select(String sql, Map<String, Object> paramMap, RowMapper<T> rowMapper);

    /**
     * 写访问（更新或插入）,参数名方式
     *
     * @param sql 所要执行的实际SQL语句
     * @param paramMap 伴随该SQL语句的参数
     * @return 更新数量
     */
    int update(String sql, Map<String, Object> paramMap);

}
