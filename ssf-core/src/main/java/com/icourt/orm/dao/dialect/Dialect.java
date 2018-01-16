package com.icourt.orm.dao.dialect;


import com.icourt.core.Page;
import com.icourt.orm.dao.SqlParams;

import java.util.Map;

/**
 * 数据库方言抽象类
 * @author june
 */
public interface Dialect{
    <T> SqlParams savePojo(T t);
    <T> SqlParams updatePojo(T t, String... updateColumns);
    <T> SqlParams updatePojoByColumn(T t, String[] updateColumns, String[] whereColumns);
    <T> SqlParams updatePojoByColumn(T t, String[] updateColumns, Map<String, Object> whereMap);
    String testQuery();
    <T> SqlParams deletePojo(T t);
    <T> SqlParams deletePojoById(Class<T> clazz, Object... ids);
    <T> SqlParams findByColumns(Class<T> clazz, String[] selectColumns, String[] whereColumns, Object... params);
    SqlParams page(Page page, String sql, Object... params);
    SqlParams pageParamMap(Page page, String sql, Map<String, Object> paramMap);
    String buildCountSql(String sql);
}
