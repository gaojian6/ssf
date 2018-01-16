/*
 * @date 2016年10月27日 15:14
 */
package com.icourt.orm.dao;

import com.icourt.core.Page;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * 通用dao
 * @author june
 */
public interface IApplicationDao {

    /**
     * 保存pojo
     * @param t 实例
     * @param <T> 类型
     * @return 保存后的对象,带主键
     */
    <T> T save(T t);

    /**
     * 更新pojo
     * @param t 实例
     * @param updateColumns 要更新的列,如果不指定,更新所有列
     * @param <T> 类型
     * @return 更新数量
     */
    <T> int update(T t, String... updateColumns);
    /**
     * 更新pojo
     * @param t 实例
     * @param updateColumns 要更新的列,如果不指定,更新所有列
     * @param <T> 类型
     * @param whereColumns 除了主键以外其他的条件字段
     * @return 更新数量
     */
    <T> int updateByColumn(T t, String[] updateColumns, String[] whereColumns);

    /**
     * 更新pojo
     * @param t 实例
     * @param updateColumns 要更新的列,如果不指定,更新所有列
     * @param <T> 类型
     * @param whereMap 除了主键以外其他的条件
     * @return 更新数量
     */
    <T> int updateByColumn(T t, String[] updateColumns, Map<String, Object> whereMap);

    /**
     * 删除pojo
     * @param t 实例,主键必须有值,否则抛出运行异常
     * @param <T> 类型
     * @return 删除数量
     */
    <T> int delete(T t);

    /**
     * 根据主键删除pojo
     * @param clazz pojo类型
     * @param ids 主键id
     * @param <T> 类型
     * @return 删除的数量
     */
    <T> int deleteById(Class<T> clazz, Object... ids);

    /**
     * 执行sql,只能执行保存，删除，更新操作
     * @param sql sql
     * @param params 参数
     * @return 影响的行数
     */
    int execute(String sql, Object... params);

    /**
     * 参数名方式执行sql,只能执行保存，删除，更新操作
     * @param paramMap 参数名
     * @param sql sql
     * @return 影响的行数
     */
    int execute(Map<String, Object> paramMap, String sql);


    //-------------------------START pojo对象相关操作方法,查询都以find开头--------------------------------

    /**
     * 根据一些列的值进行查询,并只返回指定的列
     * @param clazz pojoClass
     * @param selectColumns 要返回的列,必须是数据库列名而非pojo的字段名
     * @param whereColumns 要查询的列,必须是数据库列名而非pojo的字段名
     * @param params 查询的列的值
     * @param <T> 类型
     * @return 结果集
     */
    <T> List<T> findByColumns(Class<T> clazz, String[] selectColumns, String[] whereColumns, Object... params);

    /**
     * 根据一些列的值进行查询,返回所有列
     * @param clazz pojoClass
     * @param whereColumns 要查询的列,必须是数据库列名而非pojo的字段名
     * @param params 查询的列的值
     * @param <T> 类型
     * @return 结果集
     */
    <T> List<T> findByColumns(Class<T> clazz, String[] whereColumns, Object... params);

    /**
     * 根据某列进行查询
     * @param clazz pojoClass
     * @param whereColumn 哪一列,必须是数据库列名而非pojo的字段名
     * @param columnValue 值
     * @param <T> 类型
     * @return 结果集
     */
    <T> List<T> findByColumn(Class<T> clazz, String whereColumn, Object columnValue);

    /**
     * 根据主键查询对象，多主键必须都传
     * @param clazz pojoClass
     * @param ids 主键
     * @param <T> 类型
     * @return pojo
     */
    <T> T findById(Class<T> clazz, Object... ids);

    /**
     * 根据sql查询对象集合
     * @param clazz pojoClass
     * @param sql sql 列的别名必须是列名,不是pojo的字段名
     * @param params 参数
     * @param <T> 类型
     * @return 结果集
     */
    <T> List<T> find(Class<T> clazz, String sql, Object... params);

    /**
     * 根据sql查询对象集合,参数名方式
     * @param clazz pojoClass
     * @param paramMap 参数
     * @param sql sql 列的别名必须是列名,不是pojo的字段名
     * @param <T> 类型
     * @return 结果集
     */
    <T> List<T> find(Class<T> clazz, Map<String, Object> paramMap, String sql);

    /**
     * 查询pojo的所有数据
     * @param clazz pojoClass
     * @param <T> 类型
     * @return 结果集
     */
    <T> List<T> findAll(Class<T> clazz);

    /**
     * 根据sql查询第一条数据，建议sql加上limit 1
     * @param clazz pojoClass
     * @param sql sql 列的别名必须是列名,不是pojo的字段名
     * @param params 参数
     * @param <T> 类型
     * @return 结果
     */
    <T> T findFirst(Class<T> clazz, String sql, Object... params);

    /**
     * 参数名方式,根据sql查询第一条数据，建议sql加上limit 1
     * @param clazz pojoClass
     * @param paramMap 参数
     * @param sql sql 列的别名必须是列名,不是pojo的字段名
     * @param <T> 类型
     * @return 结果
     */
    <T> T findFirst(Class<T> clazz, Map<String, Object> paramMap, String sql);

    /**
     * 分页查询
     * @param clazz pojoClass
     * @param pageNo 当前页
     * @param pageSize 分页大小
     * @param sql sql 列的别名必须是列名,不是pojo的字段名
     * @param params 参数
     * @param <T> 类型
     * @return page
     */
    <T> Page<T> findPage(Class<T> clazz, int pageNo, int pageSize, String sql, Object... params);

    /**
     * 参数名方式,分页查询
     * @param clazz pojoClass
     * @param pageNo 当前页
     * @param pageSize 分页大小
     * @param paramMap 参数
     * @param sql sql 列的别名必须是列名,不是pojo的字段名
     * @param <T> 类型
     * @return page
     */
    <T> Page<T> findPage(Class<T> clazz, int pageNo, int pageSize, Map<String, Object> paramMap, String sql);

    /**
     * 分页查询,使用序号方式
     * @param clazz pojoClass
     * @param firstEntityIndex 开始位置
     * @param pageSize 分页大小
     * @param sql sql 列的别名必须是列名,不是pojo的字段名
     * @param params 参数
     * @param <T> 类型
     * @return page
     */
    <T> Page<T> findPage(Class<T> clazz, long firstEntityIndex, int pageSize, String sql, Object... params);

    /**
     * 参数名方式,分页查询,使用序号方式
     * @param clazz pojoClass
     * @param firstEntityIndex 开始位置
     * @param pageSize 分页大小
     * @param paramMap 参数
     * @param sql sql 列的别名必须是列名,不是pojo的字段名
     * @param <T> 类型
     * @return page
     */
    <T> Page<T> findPage(Class<T> clazz, long firstEntityIndex, int pageSize, Map<String, Object> paramMap, String sql);

    //----------------------------- pojo对象相关操作方法 END--------------------------------

    /**
     * 查询数据,返回list map
     * @param sql sql
     * @param params 参数
     * @return 结果集
     */
    List<Map<String, Object>> query(String sql, Object... params);

    /**
     * 参数名方式,查询数据,返回list map
     * @param paramMap 参数
     * @param sql sql
     * @return 结果集
     */
    List<Map<String, Object>> query(Map<String, Object> paramMap, String sql);

    /**
     * 查询数据,返回第一条,建议加上limit 1
     * @param sql sql
     * @param params 参数
     * @return 结果集
     */
    Map<String, Object> queryFirst(String sql, Object... params);

    /**
     * 参数名方式,查询数据,返回第一条,建议加上limit 1
     * @param paramMap 参数
     * @param sql sql
     * @return 结果集
     */
    Map<String, Object> queryFirst(Map<String, Object> paramMap, String sql);

    /**
     * 返回第一条数据的第一列的值
     * @param sql sql
     * @param params 参数
     * @param <T> 类型
     * @return 结果
     */
    <T> T queryForObject(String sql, Object... params);

    /**
     * 参数名方式,返回第一条数据或第一列的值
     * @param paramMap 参数
     * @param sql sql
     * @param <T> 类型
     * @return 结果
     */
    <T> T queryForObject(Map<String, Object> paramMap, String sql);

    /**
     * 查询数量
     * @param sql sql 不用写count,直接使用普通sql,如 season * from user
     * @param params 参数
     * @return 数量
     */
    long count(String sql, Object... params);

    /**
     * 参数名方式,查询数量
     * @param paramMap 参数
     * @param sql sql 不用写count,直接使用普通sql,如 season * from user
     * @return 数量
     */
    long count(Map<String, Object> paramMap, String sql);

    /**
     * 分页查询
     * @param pageNo 当前页
     * @param pageSize 分页大小
     * @param sql sql
     * @param params 参数
     * @return page
     */
    Page<Map<String, Object>> page(int pageNo, int pageSize, String sql, Object... params);

    /**
     * 参数名方式,分页查询
     * @param pageNo 当前页
     * @param pageSize 分页大小
     * @param paramMap 参数
     * @param sql sql
     * @return page
     */
    Page<Map<String, Object>> page(int pageNo, int pageSize, Map<String, Object> paramMap, String sql);

    /**
     * 分页查询,按开始位置
     * @param firstEntityIndex 开始位置
     * @param pageSize 分页大小
     * @param sql sql
     * @param params 参数
     * @return page
     */
    Page<Map<String, Object>> page(long firstEntityIndex, int pageSize, String sql, Object... params);

    /**
     * 参数名方式,分页查询,按开始位置
     * @param firstEntityIndex 开始位置
     * @param pageSize 分页大小
     * @param paramMap 参数
     * @param sql sql
     * @return page
     */
    Page<Map<String, Object>> page(long firstEntityIndex, int pageSize, Map<String, Object> paramMap, String sql);

    /**
     * 手动回滚当前事务
     */
    void rollback();

    /**
     * 获取JdbcTemplate
     * @return JdbcTemplate
     */
    JdbcTemplate getJdbcTemplate();
}
