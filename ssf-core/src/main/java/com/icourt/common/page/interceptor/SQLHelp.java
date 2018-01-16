package com.icourt.common.page.interceptor;

import com.icourt.common.page.Pagination;
import com.icourt.common.page.dialect.Dialect;
import com.icourt.common.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 功能： setParameters 设置参数 getCount 查询总记录数 generatePageSql 生成分页查询SQL
 * </p>
 *
 * @author lan
 * @version 1.0
 */
public class SQLHelp {
    protected static Logger logger = LoggerFactory.getLogger(SQLHelp.class);

    /**
     * 对SQL参数(?)设值,
     * 参考org.apache.ibatis.executor.parameter.DefaultParameterHandler。
     *
     * @param ps              表示预编译的 SQL 语句的对象。
     * @param mappedStatement MappedStatement
     * @param boundSql        SQL
     * @param parameterObject 参数对象
     * @throws SQLException 数据库异常
     */
    @SuppressWarnings("unchecked")
    public static void setParameters(PreparedStatement ps, MappedStatement mappedStatement,
                                     BoundSql boundSql, Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters")
                .object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration
                    .newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
                            && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(
                                    propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    @SuppressWarnings("rawtypes")
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter "
                                + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

    /**
     * 查询总纪录数。
     *
     * @param sql             SQL语句
     * @param connection      数据库连接
     * @param mappedStatement mapped
     * @param parameterObject 参数
     * @param boundSql        boundSql
     * @return 总记录数
     * @throws SQLException sql查询错误
     */
    public static int getCount(final String sql, final Connection connection,
                               final MappedStatement mappedStatement, final Object parameterObject,
                               final BoundSql boundSql) {
        final String countSql = "select count(1) from (" + excludeOrderBy(sql) + ") as tmp_count";
        logger.debug("Pagination TotalCount SQL: " + countSql);

        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            connection.setReadOnly(true);
            connection.setAutoCommit(true);
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = createNewBoundSql(mappedStatement, parameterObject, boundSql,
                    countSql);
            setParameters(countStmt, mappedStatement, countBS, parameterObject);
            rs = countStmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
            return 0;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("", e);
                }
            }
            if (countStmt != null) {
                try {
                    countStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("", e);
                }
            }
        }
    }

    public static MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(),
                ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        String[] keyProperties = ms.getKeyProperties();
        builder.keyProperty(StringUtils.join(keyProperties, ','));
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        return builder.build();
    }

    /**
     * 根据BoundSql 创建新 BoundSql
     *
     * @param mappedStatement
     * @param parameterObject
     * @param boundSql
     * @param newSql
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static BoundSql createNewBoundSql(final MappedStatement mappedStatement,
                                             final Object parameterObject, final BoundSql boundSql, final String newSql)
            throws NoSuchFieldException, IllegalAccessException {
        BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), newSql,
                boundSql.getParameterMappings(), parameterObject);
        Field metaParamsField = ReflectUtil.getFieldByFieldName(boundSql, "metaParameters");
        if (metaParamsField != null) {
            Object mo = ReflectUtil.getValueByFieldName(boundSql, "metaParameters");
            ReflectUtil.setValueByFieldName(countBS, "metaParameters", mo);
        }

        Field additionalParameters = ReflectUtil.getFieldByFieldName(boundSql,
                "additionalParameters");
        if (additionalParameters != null) {
            Object mo = ReflectUtil.getValueByFieldName(boundSql, "additionalParameters");
            ReflectUtil.setValueByFieldName(countBS, "additionalParameters", mo);
        }
        return countBS;
    }

    /**
     * excludeOrderBy:去除sql中的order by，避免大数据分页查询时的性能问题. <br/>
     *
     * @param sql
     * @return
     * @author yuanchao
     */
    private static String excludeOrderBy(String sql) {
        if (StringUtils.isBlank(sql)) {
            return sql;
        }
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        if (sb.length() == 0) {
            return sql;
        }
        return sb.toString();
    }

    /**
     * 根据数据库方言，生成特定的分页sql。
     *
     * @param sql     Mapper中的Sql语句
     * @param page    分页对象
     * @param dialect 方言类型
     * @return 分页SQL
     */
    public static String generatePageSql(String sql, Pagination<Serializable> page, Dialect dialect) {
        if (dialect.supportsLimit()) {
            int pageSize = page.getPageSize();
            int index = (page.getCurrentPage() - 1) * pageSize;
            int start = index < 0 ? 0 : index;
            return dialect.getLimitString(sql, start, pageSize);
        } else {
            return sql;
        }
    }
}
