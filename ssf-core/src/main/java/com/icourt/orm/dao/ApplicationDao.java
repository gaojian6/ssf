/*
 * @date 2016年10月28日 14:34
 */
package com.icourt.orm.dao;

import com.icourt.core.Page;
import com.icourt.orm.dao.dialect.AbstractDialect;
import com.icourt.orm.dao.dialect.Dialect;
import com.icourt.orm.dao.utils.PojoKit;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author june
 */
public class ApplicationDao implements IApplicationDao {

    private Statement statement;
    private DataSource dataSource;
    private String dsName;
    private Dialect dialect;

    @Override
    public <T> T save(T t) {
        //获取sql和参数
        SqlParams sqlParams = dialect.savePojo(t);
        //封装所有数据
        StatementRuntime runtime = new StatementRuntime(t,t.getClass(),sqlParams).reSetDs(dataSource,dsName);
        return statement.execute(runtime);
    }

    @Override
    public <T> int update(T t, String... updateColumns) {
        SqlParams sqlParams = dialect.updatePojo(t,updateColumns);
        StatementRuntime runtime = new StatementRuntime(t,int.class,sqlParams).reSetDs(dataSource,dsName);
        return statement.execute(runtime);
    }

    @Override
    public <T> int updateByColumn(T t, String[] updateColumns,String[] whereColumns) {
        SqlParams sqlParams = dialect.updatePojoByColumn(t,updateColumns,whereColumns);
        StatementRuntime runtime = new StatementRuntime(t,int.class,sqlParams).reSetDs(dataSource,dsName);
        return statement.execute(runtime);
    }

    @Override
    public <T> int updateByColumn(T t, String[] updateColumns,Map<String,Object> whereMap) {
        SqlParams sqlParams = dialect.updatePojoByColumn(t,updateColumns,whereMap);
        StatementRuntime runtime = new StatementRuntime(t,int.class,sqlParams).reSetDs(dataSource,dsName);
        return statement.execute(runtime);
    }

    @Override
    public <T> int delete(T t) {
        SqlParams sqlParams = dialect.deletePojo(t);
        StatementRuntime runtime = new StatementRuntime(t,int.class,sqlParams).reSetDs(dataSource,dsName);
        return statement.execute(runtime);
    }

    @Override
    public <T> int deleteById(Class<T> clazz, Object... ids) {
        SqlParams sqlParams = dialect.deletePojoById(clazz,ids);
        StatementRuntime runtime = new StatementRuntime(clazz,int.class,sqlParams).reSetDs(dataSource,dsName);
        return statement.execute(runtime);
    }

    @Override
    public int execute(String sql, Object... params) {
        StatementRuntime runtime = new StatementRuntime(sql,int.class,params).reSetDs(dataSource,dsName);
        return statement.execute(runtime);
    }

    @Override
    public int execute(Map<String, Object> paramMap, String sql) {
        StatementRuntime runtime = new StatementRuntime(sql,int.class,paramMap).reSetDs(dataSource,dsName);
        return statement.execute(runtime);
    }

    @Override
    public <T> List<T> findByColumns(Class<T> clazz, String[] selectColumns, String[] whereColumns, Object... params) {
        SqlParams sqlParams = dialect.findByColumns(clazz,selectColumns,whereColumns,params);
        StatementRuntime runtime = new StatementRuntime(clazz,List.class,sqlParams).reSetDs(dataSource,dsName);
        List<Map<String,Object>> list =  statement.execute(runtime);
        return PojoKit.build(clazz,list);
    }

    @Override
    public <T> List<T> findByColumns(Class<T> clazz, String[] whereColumns, Object... params) {
        return findByColumns(clazz,new String[]{"*"},whereColumns,params);
    }

    @Override
    public <T> List<T> findByColumn(Class<T> clazz, String whereColumn, Object columnValue) {
        return findByColumns(clazz,new String[]{whereColumn}, columnValue);
    }

    @Override
    public <T> T findById(Class<T> clazz, Object... ids) {
        Assert.notEmpty(ids);
        String[] pks = AbstractDialect.getTable(clazz).getPrimaryKey();
        List<T> resultList = this.findByColumns(clazz,pks,ids);
        return resultList.size()>0?resultList.get(0):null;
    }

    @Override
    public <T> List<T> find(Class<T> clazz, String sql, Object... params) {
        SqlParams sqlParams = new SqlParams(params,sql);
        StatementRuntime runtime = new StatementRuntime(clazz,List.class,sqlParams).reSetDs(dataSource,dsName);
        List<Map<String,Object>> list =  statement.execute(runtime);
        return PojoKit.build(clazz,list);
    }

    @Override
    public <T> List<T> find(Class<T> clazz, Map<String, Object> paramMap, String sql) {
        SqlParams sqlParams = new SqlParams(paramMap,sql);
        StatementRuntime runtime = new StatementRuntime(clazz,List.class,sqlParams).reSetDs(dataSource,dsName);
        List<Map<String,Object>> list =  statement.execute(runtime);
        return PojoKit.build(clazz,list);
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz) {
        return this.findByColumns(clazz, null);
    }

    @Override
    public <T> T findFirst(Class<T> clazz, String sql, Object... params) {
        List<T> list = find(clazz,sql,params);
        if(list==null || list.size()==0){
            return null;
        }
        return list.get(0);
    }

    @Override
    public <T> T findFirst(Class<T> clazz, Map<String, Object> paramMap, String sql) {
        List<T> list = find(clazz,paramMap,sql);
        if(list==null || list.size()==0){
            return null;
        }
        return list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Page<T> findPage(Class<T> clazz, int pageNo, int pageSize, String sql, Object... params) {
        //总数
        int count = (int) this.count(sql,params);
        Page page = new Page(pageSize,pageNo,count);
        if(count==0){
            return page;
        }
        return page(clazz,page,sql,params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Page<T> findPage(Class<T> clazz, int pageNo, int pageSize, Map<String, Object> paramMap, String sql) {
        //总数
        int count = (int) this.count(paramMap,sql);
        Page page = new Page(pageSize,pageNo,count);
        if(count==0){
            return page;
        }
        return page(clazz,page,paramMap,sql);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> Page<T> findPage(Class<T> clazz, long firstEntityIndex, int pageSize, String sql, Object... params) {
        //总数
        int count = (int) this.count(sql,params);
        Page page = new Page(pageSize,firstEntityIndex,count);
        if(count==0){
            return page;
        }
        return page(clazz,page,sql,params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Page<T> findPage(Class<T> clazz, long firstEntityIndex, int pageSize, Map<String, Object> paramMap, String sql) {
        //总数
        int count = (int) this.count(paramMap,sql);
        Page page = new Page(pageSize,firstEntityIndex,count);
        if(count==0){
            return page;
        }
        return page(clazz,page,paramMap,sql);
    }

    @SuppressWarnings("unchecked")
    private <T> Page<T> page(Class<T> clazz,Page page,String sql,Object... params){
        //获取分页sql
        SqlParams sqlParams = dialect.page(page,sql,params);
        List<T> list = this.find(clazz,sqlParams.getSql(),sqlParams.getParams());
        page.setEntities(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    private <T> Page<T> page(Class<T> clazz,Page page,Map<String, Object> paramMap,String sql){
        //获取分页sql
        SqlParams sqlParams = dialect.pageParamMap(page,sql,paramMap);
        List<T> list = this.find(clazz,sqlParams.getParamMap(),sqlParams.getSql());
        page.setEntities(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> query(String sql, Object... params) {
        StatementRuntime runtime = new StatementRuntime(sql,List.class).reSetDs(dataSource,dsName);
        runtime.setParams(params);
        return statement.execute(runtime);
    }

    @Override
    public List<Map<String, Object>> query(Map<String, Object> paramMap, String sql) {
        StatementRuntime runtime = new StatementRuntime(sql,List.class).reSetDs(dataSource,dsName);
        runtime.setParamMap(paramMap);
        return statement.execute(runtime);
    }

    @Override
    public Map<String, Object> queryFirst(String sql, Object... params) {
        List<Map<String, Object>> list = query(sql,params);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Map<String, Object> queryFirst(Map<String, Object> paramMap, String sql) {
        List<Map<String, Object>> list = query(paramMap,sql);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T queryForObject(String sql, Object... params) {
        Map<String, Object> map = this.queryFirst(sql,params);
        if(map!=null && !map.isEmpty()){
            //取第一条数据第一列的值
            Object obj =  map.get(map.keySet().toArray()[0]);
            return (T) obj;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T queryForObject(Map<String, Object> paramMap, String sql) {
        Map<String, Object> map = this.queryFirst(paramMap,sql);
        if(map!=null && !map.isEmpty()){
            //取第一条数据第一列的值
            Object obj =  map.get(map.keySet().toArray()[0]);
            return (T) obj;
        }
        return null;
    }

    @Override
    public long count(String sql, Object... params) {
        String countSql = dialect.buildCountSql(sql);
        return queryForObject(countSql,params);
    }

    @Override
    public long count(Map<String, Object> paramMap, String sql) {
        String countSql = dialect.buildCountSql(sql);
        return queryForObject(paramMap,countSql);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<Map<String, Object>> page(int pageNo, int pageSize, String sql, Object... params) {
        //总数
        int count = (int) this.count(sql,params);
        Page page = new Page(pageSize,pageNo,count);
        if(count==0){
            return page;
        }
        return page(page,sql,params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<Map<String, Object>> page(int pageNo, int pageSize, Map<String, Object> paramMap, String sql) {
        //总数
        int count = (int) this.count(paramMap,sql);
        Page page = new Page(pageSize,pageNo,count);
        if(count==0){
            return page;
        }
        return page(page,paramMap,sql);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<Map<String, Object>> page(long firstEntityIndex, int pageSize, String sql, Object... params) {
        //总数
        int count = (int) this.count(sql,params);
        Page page = new Page(pageSize,firstEntityIndex,count);
        if(count==0){
            return page;
        }
        return page(page,sql,params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<Map<String, Object>> page(long firstEntityIndex, int pageSize, Map<String, Object> paramMap, String sql) {
        //总数
        int count = (int) this.count(paramMap,sql);
        Page page = new Page(pageSize,firstEntityIndex,count);
        if(count==0){
            return page;
        }
        return page(page,paramMap,sql);
    }

    @SuppressWarnings("unchecked")
    private Page<Map<String, Object>> page(Page page,String sql,Object... params){
        //获取分页sql
        SqlParams sqlParams = dialect.page(page,sql,params);
        List<Map<String, Object>> list = this.query(sqlParams.getSql(),sqlParams.getParams());
        page.setEntities(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    private Page<Map<String, Object>> page(Page page,Map<String, Object> paramMap,String sql){
        //获取分页sql
        SqlParams sqlParams = dialect.pageParamMap(page,sql,paramMap);
        List<Map<String, Object>> list = this.query(sqlParams.getParamMap(),sqlParams.getSql());
        page.setEntities(list);
        return page;
    }

    @Override
    public void rollback() {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}
