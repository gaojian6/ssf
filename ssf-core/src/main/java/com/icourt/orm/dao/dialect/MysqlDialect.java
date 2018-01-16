package com.icourt.orm.dao.dialect;

import com.icourt.common.ArrayKit;
import com.icourt.common.ClassKit;
import com.icourt.common.StrKit;
import com.icourt.core.Page;
import com.icourt.orm.dao.SqlParams;
import com.icourt.orm.dao.Table;
import com.icourt.orm.dao.utils.SqlKit;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mysql方言
 *
 * @author june
 */
public class MysqlDialect extends AbstractDialect {

    @Override
    public <T> SqlParams savePojo(T t) {
        Table table = getTable(t.getClass());
        StringBuilder sb = new StringBuilder();
        sb.append("insert into `").append(table.getTableName()).append("`(");
        StringBuilder temp = new StringBuilder(") values(");
        List<Object> paramList = new ArrayList<>();
        //表中声明的字段列表和通过set方法设置的列
        List<String> columnList = table.getColumnList();
        if (columnList != null && columnList.size() > 0) {
            for (String columnName : columnList) {
                if (paramList.size() > 0) {
                    sb.append(", ");
                    temp.append(", ");
                }
                sb.append("`").append(columnName).append("`");
                temp.append("?");
                //获取真实属性名
                String fieldName = table.getColumnFieldMappingMap().get(columnName);
                Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+columnName);
                paramList.add(ClassKit.getValue(t, fieldName));
            }
            sb.append(temp.toString()).append(")");
        }
        return new SqlParams(paramList.toArray(), sb.toString());
    }

    @Override
    public <T> SqlParams updatePojo(T t, String... updateColumns) {
        Table table = getTable(t.getClass());
        String[] pKeys = table.getPrimaryKey();//主键
        StringBuilder sb = new StringBuilder();
        sb.append("update `").append(table.getTableName()).append("` set ");
        List<Object> paramList = new ArrayList<>();
        List<String> columnList;
        //是否指定更新列
        if (updateColumns == null || updateColumns.length == 0) {
            columnList = table.getColumnList();
        } else {
            columnList = Arrays.asList(updateColumns);
        }
        //表中声明的字段列表和通过set方法设置的列
        if (columnList != null && columnList.size() > 0) {
            for (String columnName : columnList) {
                //遍历主键，如果是主键不参与修改字段
                boolean isPKey = false;
                for (String pKey : pKeys) {
                    if (pKey != null && pKey.equalsIgnoreCase(columnName)) {
                        isPKey = true;//主键字段返回
                    }
                }
                if (isPKey) {//是主键字段返回
                    continue;
                }
                if (paramList.size() > 0) {
                    sb.append(", ");
                }
                sb.append("`").append(columnName).append("`= ?");
                //获取真实属性名
                String fieldName = table.getColumnFieldMappingMap().get(columnName);
                Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+columnName);
                paramList.add(ClassKit.getValue(t, fieldName));
            }

            //构建where条件
            StringBuilder sWhereStr = new StringBuilder("");
            for (String pKey : pKeys) {
                //主键为空
                if (StrKit.isEmpty(pKey)) {
                    return null;
                }
                //获取真实属性名
                String fieldName = table.getColumnFieldMappingMap().get(pKey);
                Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+pKey);
                //主键的值为空
                Object obj = ClassKit.getValue(t, fieldName);
                if (obj == null) {
                    return null;
                }
                if (StrKit.isEmpty(sWhereStr.toString())) {//还没有主键where条件
                    sWhereStr.append(" where `").append(pKey).append("`= ?");
                } else {//有一个主键条件了
                    sWhereStr.append(" and `").append(pKey).append("`= ?");
                }
                paramList.add(obj);
            }
            sb.append(sWhereStr);
        }
        return new SqlParams(paramList.toArray(), sb.toString());
    }

    @Override
    public <T> SqlParams updatePojoByColumn(T t, String[] updateColumns,String[] whereColumns) {
        Table table = getTable(t.getClass());
        String[] pKeys = table.getPrimaryKey();//主键
        StringBuilder sb = new StringBuilder();
        sb.append("update `").append(table.getTableName()).append("` set ");
        List<Object> paramList = new ArrayList<>();
        List<String> columnList;
        //是否指定更新列
        if (updateColumns == null || updateColumns.length == 0) {
            columnList = table.getColumnList();
        } else {
            columnList = Arrays.asList(updateColumns);
        }
        Assert.notEmpty(columnList,t.getClass().getName()+"不存在更新字段");
        Assert.notEmpty(whereColumns,t.getClass().getName()+"不存在条件字段");
        for (String columnName : columnList) {
            //遍历主键，如果是主键不参与修改字段
            boolean isPKey = false;
            for (String pKey : pKeys) {
                if (pKey != null && pKey.equalsIgnoreCase(columnName)) {
                    isPKey = true;//主键字段返回
                }
            }
            if (isPKey) {//是主键字段返回
                continue;
            }
            if (paramList.size() > 0) {
                sb.append(", ");
            }
            sb.append("`").append(columnName).append("`= ?");
            //获取真实属性名
            String fieldName = table.getColumnFieldMappingMap().get(columnName);
            Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+columnName);
            paramList.add(ClassKit.getValue(t, fieldName));
        }

        //构建where条件
        StringBuilder sWhereStr = new StringBuilder("");
        for (String pKey : pKeys) {
            //主键为空
            if (StrKit.isEmpty(pKey)) {
                return null;
            }
            //获取真实属性名
            String fieldName = table.getColumnFieldMappingMap().get(pKey);
            Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+pKey);
            //主键的值为空
            Object obj = ClassKit.getValue(t, fieldName);
            if (obj == null) {
                return null;
            }
            if (StrKit.isEmpty(sWhereStr.toString())) {//还没有主键where条件
                sWhereStr.append(" where `").append(pKey).append("`= ?");
            } else {//有一个主键条件了
                sWhereStr.append(" and `").append(pKey).append("`= ?");
            }
            paramList.add(obj);
        }
        for (String whereColumn : whereColumns) {
            //获取真实属性名
            String fieldName = table.getColumnFieldMappingMap().get(whereColumn);
            Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+whereColumn);
            //值为空
            Object obj = ClassKit.getValue(t, fieldName);
            if (obj == null) {
                sWhereStr.append(" and `").append(fieldName).append("`is null");
            }else{
                sWhereStr.append(" and `").append(fieldName).append("`= ?");
                paramList.add(obj);
            }
        }
        sb.append(sWhereStr);
        return new SqlParams(paramList.toArray(), sb.toString());
    }


    @Override
    public <T> SqlParams updatePojoByColumn(T t, String[] updateColumns,Map<String,Object> whereMap) {
        Table table = getTable(t.getClass());
        String[] pKeys = table.getPrimaryKey();//主键
        StringBuilder sb = new StringBuilder();
        sb.append("update `").append(table.getTableName()).append("` set ");
        List<Object> paramList = new ArrayList<>();
        List<String> columnList;
        //是否指定更新列
        if (updateColumns == null || updateColumns.length == 0) {
            columnList = table.getColumnList();
        } else {
            columnList = Arrays.asList(updateColumns);
        }
        Assert.notEmpty(columnList,t.getClass().getName()+"不存在更新字段");
        for (String columnName : columnList) {
            //遍历主键，如果是主键不参与修改字段
            boolean isPKey = false;
            for (String pKey : pKeys) {
                if (pKey != null && pKey.equalsIgnoreCase(columnName)) {
                    isPKey = true;//主键字段返回
                }
            }
            if (isPKey) {//是主键字段返回
                continue;
            }
            if (paramList.size() > 0) {
                sb.append(", ");
            }
            sb.append("`").append(columnName).append("`= ?");
            //获取真实属性名
            String fieldName = table.getColumnFieldMappingMap().get(columnName);
            Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+columnName);
            paramList.add(ClassKit.getValue(t, fieldName));
        }

        //构建where条件
        StringBuilder sWhereStr = new StringBuilder("");
        for (String pKey : pKeys) {
            //主键为空
            if (StrKit.isEmpty(pKey)) {
                return null;
            }
            //获取真实属性名
            String fieldName = table.getColumnFieldMappingMap().get(pKey);
            Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+pKey);
            //主键的值为空
            Object obj = ClassKit.getValue(t, fieldName);
            if (obj == null) {
                return null;
            }
            if (StrKit.isEmpty(sWhereStr.toString())) {//还没有主键where条件
                sWhereStr.append(" where `").append(pKey).append("`= ?");
            } else {//有一个主键条件了
                sWhereStr.append(" and `").append(pKey).append("`= ?");
            }
            paramList.add(obj);
        }
        for (String whereColumn : whereMap.keySet()) {
            //获取真实属性名
            String fieldName = table.getColumnFieldMappingMap().get(whereColumn);
            Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+whereColumn);
            //值为空
            Object obj = whereMap.get(whereColumn);
            if (obj == null) {
                sWhereStr.append(" and `").append(fieldName).append("`is null");
            }else{
                sWhereStr.append(" and `").append(fieldName).append("`= ?");
                paramList.add(obj);
            }
        }
        sb.append(sWhereStr);
        return new SqlParams(paramList.toArray(), sb.toString());
    }




    @Override
    public <T> SqlParams deletePojo(T t) {
        Table table = getTable(t.getClass());
        String[] pKeys = table.getPrimaryKey();
        StringBuilder sb = new StringBuilder(45);
        sb.append("delete from `");
        sb.append(table.getTableName());
        sb.append("` where ");
        Object[] params = new Object[pKeys.length];
        for (int i = 0; i < pKeys.length; i++) {
            //主键为空
            if (StrKit.isEmpty(pKeys[i])) {
                return null;
            }
            //获取真实属性名
            String fieldName = table.getColumnFieldMappingMap().get(pKeys[i]);
            Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+pKeys[i]);
            //主键的值为空
            Object value = ClassKit.getValue(t, fieldName);
            if (value == null) {
                return null;
            }
            if (i > 0) {
                sb.append(" and ");
            }
            sb.append("`").append(pKeys[i]).append("` = ?");
            params[i] = value;
        }

        return new SqlParams(params, sb.toString());
    }

    @Override
    public <T> SqlParams deletePojoById(Class<T> clazz, Object... ids) {
        Table table = getTable(clazz);
        String[] pKeys = table.getPrimaryKey();
        if (pKeys.length != ids.length) {
            throw new IllegalArgumentException("参数的个数必须和主键的个数相等");
        }
        StringBuilder sb = new StringBuilder(45);
        sb.append("delete from `");
        sb.append(table.getTableName() + "`");
        for (int i = 0; i < pKeys.length; i++) {
            if (i == 0) {
                sb.append(" where ");
            } else {
                sb.append(" and ");
            }
            sb.append(" `" + pKeys[i] + "` = ? ");
        }
        return new SqlParams(ids, sb.toString());
    }

    @Override
    public <T> SqlParams findByColumns(Class<T> clazz, String[] selectColumns, String[] whereColumns, Object... params) {

        if (whereColumns != null && whereColumns.length != params.length) {
            throw new IllegalArgumentException("列数与传入值个数不相等！");
        }

        StringBuilder findSql = new StringBuilder();

        // 获取表信息
        Table table = getTable(clazz);

        String selectStr = "";
        if (selectColumns.length == 1 && selectColumns[0].equals("*")) {
            selectStr = "*";
        } else {
            for (int i = 0; i < selectColumns.length; i++) {
                if (i > 0) {
                    selectStr += ",";
                }
                selectStr += "`" + selectColumns[i] + "`";
            }
        }
        findSql.append("select " + selectStr + " from `");
        findSql.append(table.getTableName() + "`");

        if (whereColumns != null && whereColumns.length > 0) {
            for (int i = 0; i < whereColumns.length; i++) {
                if (i == 0) {
                    findSql.append(" where ");
                } else {
                    findSql.append(" and ");
                }
                findSql.append("`" + whereColumns[i] + "` = ? ");
            }
        }
        return new SqlParams(params, findSql.toString());
    }

    @Override
    public SqlParams page(Page page, String sql, Object... params) {
        StringBuffer sb = new StringBuffer(sql);
        sb.append(" limit ?,? ");
        Object[] pageParams = new Object[]{page.getFirstEntityIndex(), page.getPageSize()};
        if (params != null && params.length > 0) {
            List sqlParamsList = ArrayKit.asList(pageParams);
            List paramsList = ArrayKit.asList(params);
            paramsList.addAll(sqlParamsList);

            params = paramsList.toArray();
        } else {
            params = pageParams;
        }
        return new SqlParams(params, sb.toString());
    }

    /**
     * 参数化page
     *
     * @param page
     * @param sql
     * @return
     */
    @Override
    public SqlParams pageParamMap(Page page, String sql, Map<String, Object> paramMap) {
        StringBuffer sb = new StringBuffer(sql);
        sb.append(" limit :startIndex,:endIndex ");
        Map<String, Object> pageParamMap = new HashMap<String, Object>();
        pageParamMap.put("startIndex", page.getFirstEntityIndex());
        pageParamMap.put("endIndex", page.getPageSize());
        if (paramMap != null) {
            paramMap.putAll(pageParamMap);
        } else {
            paramMap = pageParamMap;
        }
        SqlParams sqlParams = new SqlParams();
        sqlParams.setSql(sb.toString());
        sqlParams.setParamMap(paramMap);
        return sqlParams;
    }

    @Override
    public String buildCountSql(String sql) {
        return "select count(*) from (" + SqlKit.replaceFormatSqlOrderBy(sql) + ") as count_select_";
    }


    @Override
    public String testQuery() {
        return "select 1";
    }
}
