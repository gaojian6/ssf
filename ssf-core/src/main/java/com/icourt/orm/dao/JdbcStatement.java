/*
 * @date 2016年10月28日 17:47
 */
package com.icourt.orm.dao;

import com.icourt.common.ClassKit;
import com.icourt.orm.dao.dialect.AbstractDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author june
 */
public class JdbcStatement implements Statement{

    private final List<Interpreter> interpreters;


    private Logger logger = LoggerFactory.getLogger(JdbcStatement.class);

    public JdbcStatement(List<Interpreter> interpreters){
        Assert.notEmpty(interpreters);
        this.interpreters = interpreters;
    }

    @Override
    public <T> T execute(StatementRuntime statementRuntime) {
        //执行sql解释器
        for (Interpreter interpreter : interpreters) {
            interpreter.interpret(statementRuntime);
        }
        Object resultObj;
        Class returnType = statementRuntime.getReturnType();

        IDataAccess dataAccess = new DataAccess(statementRuntime.getDataSource());
        SQLType sqlType = statementRuntime.getSqlType();
        String sql = statementRuntime.getSql();
        Map<String,Object> paramMap = statementRuntime.getParamMap();
        Object[] args = statementRuntime.getParams();
        RowMapper rowMapper = new RowMapperFactory().getRowMapper(statementRuntime);
        //记录sql执行时间
        long startTime = System.currentTimeMillis();
        if(sqlType==SQLType.READ){
            //使用参数名方式
            if(paramMap!=null){
                resultObj = dataAccess.select(sql,paramMap,rowMapper);
//                resultObj = convertSelectResult(statementRuntime,dataAccess.select(sql,paramMap,rowMapper));
            }else{
                resultObj = dataAccess.select(sql,args,rowMapper);
//                resultObj = convertSelectResult(statementRuntime,dataAccess.select(sql,args,rowMapper));
            }
        }else{
            resultObj = executeSingle(statementRuntime,dataAccess);
        }
        //打印sql耗时
        long endTime = System.currentTimeMillis();
        long time = endTime-startTime;
        if(paramMap!=null){
            SqlReporter.doReport(statementRuntime.getSql(),paramMap,time,statementRuntime.getDsName());
        }else{
            SqlReporter.doReport(statementRuntime.getSql(),statementRuntime.getParams(),time,statementRuntime.getDsName());
        }
        if (returnType == void.class || resultObj==null) {
            return null;
        }
        return (T) resultObj;
    }

    /**
     * 转换查询返回的结果集
     * @param runtime sql相关
     * @param listResult 结果集
     * @return 转换后的结果
     */
    private Object convertSelectResult(StatementRuntime runtime, List<?> listResult){
        Class returnType = runtime.getReturnType();
        if (List.class == returnType || Collection.class == returnType || Iterable.class == returnType) {
            return listResult;
        }
        if (ArrayList.class == returnType){
            return new ArrayList(listResult);
        }
        if (LinkedList.class == returnType) {
            return new LinkedList(listResult);
        }
        if (Set.class == returnType || HashSet.class == returnType){
            return new HashSet(listResult);
        }
        if (Collection.class.isAssignableFrom(returnType)) {
            Collection listToReturn;
            try {
                listToReturn = (Collection) returnType.newInstance();
            } catch (Exception e) {
                throw new Error("error to create instance of " + returnType.getName());
            }
            listToReturn.addAll(listResult);
            return listToReturn;
        }
        if (Iterator.class == returnType) {
            return listResult.iterator();
        }
        if (returnType.isArray() && byte[].class != returnType){
            if (returnType.getComponentType().isPrimitive()) {
                Object array = Array.newInstance(returnType.getComponentType(), listResult.size());
                int len = listResult.size();
                for (int i = 0; i < len; i++) {
                    Array.set(array, i, listResult.get(i));
                }
                return array;
            }
            else {
                Object array = Array.newInstance(returnType.getComponentType(), listResult.size());
                return listResult.toArray((Object[]) array);
            }
        }
        if (Map.class == returnType || HashMap.class == returnType){
            return convertMap(new HashMap(),listResult);
        }
        if (Hashtable.class == returnType){
            return convertMap(new Hashtable(),listResult);
        }
        if (Map.class.isAssignableFrom(returnType)){
            try {
                Map map = (Map) returnType.newInstance();
                return convertMap(map,listResult);
            } catch (Exception e) {
                throw new Error("error to create instance of " + returnType.getName());
            }
        }
        final int sizeResult = listResult.size();
        if (sizeResult == 1) {
            // 返回单个  Bean、Boolean等类型对象
            return listResult.get(0);
        } else if (sizeResult == 0) {
            // 基础类型的抛异常，其他的返回null
            if (returnType.isPrimitive()) {
                String msg = "Incorrect result size: expected 1, actual " + sizeResult + ": "
                        + runtime.getSql();
                throw new EmptyResultDataAccessException(msg, 1);
            } else {
                return null;
            }
        } else {
            // IncorrectResultSizeDataAccessException
            String msg = "Incorrect result size: expected 0 or 1, actual " + sizeResult + ": "
                    + runtime.getSql();
            throw new IncorrectResultSizeDataAccessException(msg, 1, sizeResult);
        }
    }

    /**
     * 转换map类型的返回
     * @param map map
     * @param listResult listResult
     * @return 转换后的map
     */
    private Map convertMap(Map map,List<?> listResult){
        for (Object obj : listResult) {
            if (obj == null) {
                continue;
            }

            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;

            if (map.getClass() == Hashtable.class && entry.getKey() == null) {
                continue;
            }

            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * 执行单个更新或新增操作
     * @param runtime sql相关
     * @param dataAccess dataAccess
     * @return 结果
     */
    private Object executeSingle(StatementRuntime runtime,IDataAccess dataAccess) {
        Number result;
        Class returnType = runtime.getReturnType();
        if (returnType.isPrimitive()) {
            returnType = ClassKit.primitiveToWrapper(returnType);
        }
        Object pojo = runtime.getPojo();
        Map<String,Object> paramMap = runtime.getParamMap();
        if(paramMap!=null){
            result = new Integer(dataAccess.update(runtime.getSql(),paramMap));
        }else {
            //需要返回自动生成的key
            if (runtime.getPojoClass() == returnType && pojo != null) {
                ArrayList<Map<String, Object>> keys = new ArrayList<>(1);
                KeyHolder generatedKeyHolder = new GeneratedKeyHolder(keys);
                dataAccess.update(runtime.getSql(), runtime.getParams(), generatedKeyHolder);
                //处理自动生成的主键
                List<Map<String,Object>> list = generatedKeyHolder.getKeyList();
                Table table = AbstractDialect.getTable(runtime.getPojoClass());
                String[] pks = table.getPrimaryKey();
                for(int i=0;i<pks.length;i++){
                    Map<String,Object> map = list.get(i);
                    Object value = map.get(map.keySet().toArray(new String[0])[0]);
                    String columnName = pks[i];
                    String fieldName = table.getColumnFieldMappingMap().get(columnName);
                    if(table.hasColumn(columnName)){
                        //获取真正的类型的值
                        value = ClassKit.getObjectValue(value, table.getColumnTypeMap().get(columnName).getName());
                        ClassKit.inject(pojo, fieldName, value);
                    }
                }
                return pojo;
            } else {
                result = new Integer(dataAccess.update(runtime.getSql(), runtime.getParams(), null));
            }
        }
        if (returnType == void.class) {
            return null;
        }
        if (returnType == result.getClass()) {
            return result;
        }
        // 将结果转成方法的返回类型
        if (returnType == Integer.class) {
            return result.intValue();
        } else if (returnType == Long.class) {
            return result.longValue();
        } else if (returnType == Boolean.class) {
            return result.intValue() > 0 ? Boolean.TRUE : Boolean.FALSE;
        } else if (returnType == Double.class) {
            return result.doubleValue();
        } else if (returnType == Float.class) {
            return result.floatValue();
        } else if (returnType == Number.class) {
            return result;
        } else if (returnType == String.class || returnType == CharSequence.class) {
            return String.valueOf(result);
        } else {
            throw new DataRetrievalFailureException(
                    "The generated key is not of a supported numeric type: " + returnType.getName());
        }
    }
}
