package com.icourt.orm.dao;


import com.icourt.common.AopTargetKit;
import com.icourt.common.ClassKit;
import com.icourt.common.PropKit;
import com.icourt.common.StrKit;
import com.icourt.orm.dao.annotation.Column;
import com.icourt.orm.dao.annotation.TableInfo;
import com.icourt.orm.dao.annotation.Transient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author june
 * 2015年07月25日 10:20
 */
public class TableBuilder {

    /**
     * 根据实体类，构建表信息
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Table build(T t){
        Class<?> clazz = AopTargetKit.getDoradoProxyTargetClass(t);
        String tableName = getTableName(clazz);
        String pkName = getPkName(clazz);
        Table table = new Table();
        table.setPrimaryKey(pkName.split(","));
        table.setTableName(tableName);
        build(clazz,table);
        return table;
    }

    /**
     * 构建字段列表、字段类型、字段和列的映射关系
     * @param clz
     * @param table
     * @return
     */
    private static void build(Class<?> clz, Table table){
        Map<String, Class<?>> returnFieldMap = new HashMap<String, Class<?>>();
        List<String> columnList = new ArrayList<>();
        Map<String, String> columnFieldMappingMap = new HashMap<String, String>();
        //字段类型
        Map<String, Class<?>> fieldMap =  ClassKit.getFieldMap(clz);
        //字段列表
        List<Field> fieldList = ClassKit.getAllFields(clz,null);
        for (Field field : fieldList){
            String columnName;
            String fieldName = field.getName();
            Transient transients = field.getAnnotation(Transient.class);
            if(transients!=null){
                continue;
            }
            //序列化字段
            if(fieldName.toLowerCase().equals("serialVersionUID")){
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if(column!=null){
                columnName = column.name();
            }else{
                columnName = fieldName;
            }
            columnList.add(columnName);
            returnFieldMap.put(columnName,fieldMap.get(fieldName));
            columnFieldMappingMap.put(columnName,fieldName);
        }
        table.setColumnTypeMap(returnFieldMap);
        table.setColumnList(columnList);
        table.setColumnFieldMappingMap(columnFieldMappingMap);
    }

    /**
     * 获取表名
     * @param clazz
     * @return
     */
    private static String getTableName(Class<?> clazz) {
        TableInfo tableInfo = clazz.getAnnotation(TableInfo.class);
        if(tableInfo != null){
            String tableName = tableInfo.tableName();
            //替换配置信息
            tableName = PropKit.replaceProp(tableName);
            return tableName;
        }
        //如果没有标注，默认是类的名称，首字母小写
        String className = clazz.getName();
        return StrKit.lowerCase(className.substring(className.lastIndexOf(".") + 1));
    }

    /**
     * 获取主键
     * @param clazz
     * @return
     */
    private static String getPkName(Class<?> clazz) {
        TableInfo tableInfo = clazz.getAnnotation(TableInfo.class);
        if(tableInfo != null && StrKit.isNotEmpty(tableInfo.pkName())){
            return tableInfo.pkName();
        }
        //如果没有标注，默认是id
        return "id";
    }
}
