package com.icourt.common.page.interceptor;

import com.icourt.common.page.BasePagination;
import com.icourt.common.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Properties;


/**
 * <p>
 * 数据库分页插件，只拦截查询语句.
 * </p>
 *
 * @author lan
 * @version 1.0
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
@Slf4j
public class PaginationInterceptor extends BaseInterceptor {

    private static final long serialVersionUID = 3576678797374122941L;

    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (mappedStatement.getId().matches(sqlPattern)) { // 拦截需要分页的SQL
            Object parameter = invocation.getArgs()[1];
            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            String originalSql = boundSql.getSql().trim();
            Object parameterObject = boundSql.getParameterObject();
            if (boundSql.getSql() == null || "".equals(boundSql.getSql())) {
                return null;
            }

            // 分页参数--上下文传参
            Pagination<Serializable> page = null;
            // PageContext context = PageContext.getPageContext();

            // map传参每次都将currentPage重置,先判读map再判断context
            if (parameterObject != null) {
                page = convertParameter(parameterObject, page);
            }
            // 不需要分页
            if (page instanceof BasePagination && ((BasePagination) page).isNeedPage == 0) {
                return invocation.proceed();
            }
            // 分页参数--context参数里的Page传参
            // if (page == null) {
            // page = context;
            // }
            // 后面用到了context的东东
            if (page != null) {
                int totPage = page.getTotal();
                // 得到总记录数
                if (totPage <= 0) {
                    Connection connection = mappedStatement.getConfiguration().getEnvironment()
                            .getDataSource().getConnection();
                    totPage = SQLHelp.getCount(originalSql, connection, mappedStatement,
                            parameterObject, boundSql);
                }

                // 分页计算
                page.init(totPage, page.getPageSize(), page.getCurrentPage());

                // 分页查询 本地化对象 修改数据库注意修改实现

                String pageSql = SQLHelp.generatePageSql(originalSql, page, dialect);
                log.info("分页SQL:" + pageSql);
                invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET,
                        RowBounds.NO_ROW_LIMIT);
                BoundSql newBoundSql = SQLHelp.createNewBoundSql(mappedStatement,
                        boundSql.getParameterObject(), boundSql, pageSql);
                MappedStatement newMs = SQLHelp.copyFromMappedStatement(mappedStatement,
                        new BoundSqlSqlSource(newBoundSql));

                invocation.getArgs()[0] = newMs;
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        super.initProperties(properties);
    }


    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    public static void main(String[] args) {
        PaginationInterceptor interceptor = new PaginationInterceptor();
        interceptor.sqlPattern = "^findPag*.*";
        System.out.println("findPageABCddd".matches(interceptor.sqlPattern));
        System.out.println("dddfindPageABCddd".matches(interceptor.sqlPattern));
    }
}
