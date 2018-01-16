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
 *
 */
public class OracleDialect extends AbstractDialect {

    @Override
    public <T> SqlParams savePojo(T t) {
        Table table = getTable(t.getClass());
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(table.getTableName()).append(" (");
        StringBuilder temp = new StringBuilder(") values(");
        List<Object> paramList = new ArrayList<Object>();
        //表中声明的字段列表和通过set方法设置的列
        List<String> columnList = table.getColumnList();

        if (columnList != null && columnList.size() > 0) {
            for (String columnName : columnList) {

                //获取真实属性名
                String fieldName = table.getColumnFieldMappingMap().get(columnName);
                Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+columnName);
                // 如果为主键，并且值为空的话，不进行sql处理
                if (isPrimaryKey(columnName, table.getPrimaryKey())
                        && ClassKit.getValue(t, fieldName) == null)
                    continue;

                if (paramList.size() > 0) {
                    sb.append(", ");
                    temp.append(", ");
                }
                sb.append(columnName).append(" ");
                temp.append("?");
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
        sb.append("update ").append(table.getTableName()).append(" set ");
        List<Object> paramList = new ArrayList<Object>();
        List<String> columnList = null;
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
                sb.append("").append(columnName).append("= ?");
                //获取真实属性名
                String fieldName = table.getColumnFieldMappingMap().get(columnName);
                Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+columnName);
                paramList.add(ClassKit.getValue(t, fieldName));
            }

            //构建where条件
            StringBuffer sWhereStr = new StringBuffer("");
            for (String pKey : pKeys) {
                //主键为空
                if (StrKit.isEmpty(pKey)) {
                    return null;
                }
                //主键的值为空
                //获取真实属性名
                String fieldName = table.getColumnFieldMappingMap().get(pKey);
                Assert.notNull(fieldName,t.getClass().getName()+"不存在字段："+pKey);
                Object obj = ClassKit.getValue(t, fieldName);
                if (obj == null) {
                    return null;
                }
                if (StrKit.isEmpty(sWhereStr.toString())) {//还没有主键where条件
                    sWhereStr.append(" where " + pKey + "= ?");
                } else {//有一个主键条件了
                    sWhereStr.append(" and " + pKey + "= ?");
                }
                paramList.add(obj);
            }
            sb.append(sWhereStr);
        }
        return new SqlParams(paramList.toArray(), sb.toString());
    }

    @Override
    public <T> SqlParams updatePojoByColumn(T t, String[] updateColumns, String[] whereColumns) {
        throw new RuntimeException("该oracle方言暂时未实现，请联系ssf框架开发人员进行扩展");
    }

    @Override
    public <T> SqlParams updatePojoByColumn(T t, String[] updateColumns, Map<String,Object> whereMap) {
        throw new RuntimeException("该oracle方言暂时未实现，请联系ssf框架开发人员进行扩展");
    }

    @Override
    public <T> SqlParams deletePojo(T t) {
        Table table = getTable(t.getClass());
        String[] pKeys = table.getPrimaryKey();
        StringBuilder sb = new StringBuilder(45);
        sb.append("delete from ");
        sb.append(table.getTableName());
        sb.append(" where ");
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
//            Object value = t.get(pKeys[i]);
            Object value = ClassKit.getValue(t, fieldName);
            if (value == null) {
                return null;
            }
            if (i > 0) {
                sb.append(" and ");
            }
            sb.append("").append(pKeys[i]).append(" = ?");
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
        sb.append("delete from ");
        sb.append(table.getTableName() + "");
        for (int i = 0; i < pKeys.length; i++) {
            if (i == 0) {
                sb.append(" where ");
            } else {
                sb.append(" and ");
            }
            sb.append(" " + pKeys[i] + " = ? ");
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
                selectStr += "" + selectColumns[i] + "";
            }
        }
        findSql.append("select " + selectStr + " from ");
        findSql.append(table.getTableName() + "");

        if (whereColumns != null && whereColumns.length > 0) {
            for (int i = 0; i < whereColumns.length; i++) {
                if (i == 0) {
                    findSql.append(" where ");
                } else {
                    findSql.append(" and ");
                }
                findSql.append("" + whereColumns[i] + " = ? ");
            }
        }
        return new SqlParams(params, findSql.toString());
    }

    @Override
    public SqlParams page(Page page, String sql, Object... params) {

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT * FROM ");
        sb.append("( SELECT A.*, ROWNUM RN FROM ");
        sb.append("(").append(sql).append(") A ");
        sb.append("WHERE ROWNUM <= ? ) WHERE RN > ?");

        Object[] pageParams = new Object[]{page.getFirstEntityIndex() + page.getPageSize(), page.getFirstEntityIndex()};

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

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT * FROM ");
        sb.append("( SELECT A.*, ROWNUM RN FROM ");
        sb.append("(").append(sql).append(") A ");
        sb.append("WHERE ROWNUM <= endIndex ) WHERE RN > startIndex");


        Map<String, Object> pageParamMap = new HashMap<String, Object>();
        pageParamMap.put("startIndex", page.getFirstEntityIndex());
        pageParamMap.put("endIndex", page.getPageSize() + page.getFirstEntityIndex());
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

    /**
     * 构建查询数量的sql
     * @param sql
     * @return
     */
    @Override
    public String buildCountSql(String sql){
        return "select count(*)  as count_select_ from (" + SqlKit.replaceFormatSqlOrderBy(sql) + ")";
    }



    @Override
    public String testQuery() {
        return "select 1 FROM DUAL";
    }

    /**
     * 判断列是否为主键列
     *
     * @param columnName 列名
     * @param pkNames    所有主键
     * @return 是否为主键
     */
    private boolean isPrimaryKey(String columnName, String[] pkNames) {

        for (String pkName : pkNames)
            if (pkName.equals(columnName))
                return true;

        return false;
    }
}
