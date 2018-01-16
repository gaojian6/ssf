package com.icourt.orm.dao;


import com.icourt.common.StrKit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体类的表信息，只包含声明的字段
 * @author june
 * 2015年07月25日 07:30
 */
public class Table implements Serializable {

    private String tableName;//表名
    private String[] primaryKey;//主键集合
    private List<String> columnList = new ArrayList<String>();//所有列集合
    private Map<String, Class<?>> columnTypeMap = new HashMap<String, Class<?>>();//列的类型
    private Map<String, String> columnFieldMappingMap = new HashMap<String, String>();//列名和字段的映射关系


    /**
     * 添加列
     * @param columnName
     * @param fieldName
     * @param clazz
     * @return
     */
    public Table addColumn(String columnName, String fieldName, Class<?> clazz){
        if(StrKit.isEmpty(columnName)){
            return this;
        }
        if(!columnList.contains(columnName)){
            columnList.add(columnName);
            columnTypeMap.put(columnName,clazz);
            if(StrKit.isNotEmpty(fieldName)){
                columnFieldMappingMap.put(columnName,fieldName);
            }
        }
        return this;
    }

    /**
     * 是否包含某列
     * @param columnName
     * @return
     */
    public boolean hasColumn(String columnName){
        if(columnList!=null){
            return columnList.contains(columnName);
        }
        return false;
    }

    public List<String> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }

    public Map<String, Class<?>> getColumnTypeMap() {
        return columnTypeMap;
    }

    public void setColumnTypeMap(Map<String, Class<?>> columnTypeMap) {
        this.columnTypeMap = columnTypeMap;
    }

    public String[] getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String[] primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getColumnFieldMappingMap() {
        return columnFieldMappingMap;
    }

    public void setColumnFieldMappingMap(Map<String, String> columnFieldMappingMap) {
        this.columnFieldMappingMap = columnFieldMappingMap;
    }

    @Override
    public String toString() {
        return "Table{" +
                "columnList=" + columnList +
                ", tableName='" + tableName + '\'' +
                ", primaryKey=" + Arrays.toString(primaryKey) +
                ", columnTypeMap=" + columnTypeMap +
                '}';
    }
}
