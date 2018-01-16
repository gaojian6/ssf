/*
 * @date 2016年10月28日 10:37
 */
package com.icourt.orm.dao;

import com.icourt.core.Page;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author june
 */
public class StatementRuntime {

    //sql
    private String sql;

    //参数,sql中的？
    private Object[] params;

    //参数化的值
    private Map<String,Object> paramMap;

    /**
     * 返回{@link Interpreter}在解析SQL语句时候，可以给runtime设置的一些属性
     */
    private Map<String, Object> attributes;

    //分页信息
    private Page page;

    //sql类型
    private SQLType sqlType;

    //当前操作的pojo的class
    private Class pojoClass;

    //当前操作的pojo
    private Object pojo;

    //方法返回类型
    private Class returnType;

    private DataSource dataSource;
    private String dsName;

    public StatementRuntime(){


    }

    public StatementRuntime(String sql,Class returnType){
        this();
        Assert.notNull(sql);
        Assert.notNull(returnType);
        this.sql = sql;
        this.returnType = returnType;
        this.sqlType = resolveSQLType(sql);
    }

    public StatementRuntime(Class pojoClass,Class returnType,SqlParams sqlParams){
        this(sqlParams.getSql(),returnType);
        Assert.notNull(pojoClass);
        this.pojoClass = pojoClass;
        this.paramMap = sqlParams.getParamMap();
        this.params = sqlParams.getParams();
    }

    public StatementRuntime(Object pojo,Class returnType,SqlParams sqlParams){
        this(pojo.getClass(),returnType,sqlParams);
        Assert.notNull(pojo);
        this.pojo = pojo;
    }

    public StatementRuntime(String sql,Class returnType,Object[] params){
        this(sql,returnType);
        this.params = params;
    }

    public StatementRuntime(String sql,Class returnType,Map<String,Object> paramMap){
        this(sql,returnType);
        this.paramMap = paramMap;
    }


    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SQLType sqlType) {
        this.sqlType = sqlType;
    }

    public Class getPojoClass() {
        return pojoClass;
    }

    public void setPojoClass(Class pojoClass) {
        this.pojoClass = pojoClass;
    }

    public Object getPojo() {
        return pojo;
    }

    public void setPojo(Object pojo) {
        this.pojo = pojo;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    /**
     * 根据sql获取 SQLType
     * @param sql sql
     * @return SQLType
     */
    private SQLType resolveSQLType(String sql) {
        SQLType sqlType = SQLType.WRITE;
        // 用正则表达式匹配 SELECT 语句
        if (Pattern.compile("^\\s*SELECT.*",Pattern.CASE_INSENSITIVE).matcher(sql).find()) {
            sqlType = SQLType.READ;
        }
        return sqlType;
    }

    public StatementRuntime reSetDs(DataSource dataSource,String dsName){
        this.setDataSource(dataSource);
        this.setDsName(dsName);
        return this;
    }

}
