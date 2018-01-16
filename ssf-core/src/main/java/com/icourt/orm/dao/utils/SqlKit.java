package com.icourt.orm.dao.utils;

import com.alibaba.druid.util.JdbcUtils;
import com.icourt.orm.dao.dialect.Dialect;
import com.icourt.orm.dao.dialect.MysqlDialect;
import com.icourt.orm.dao.dialect.OracleDialect;
import org.springframework.util.StringUtils;

/**
 * sql相关工具类
 * @author june
 */
public class SqlKit {

    /**
     * 根据url获取数据库的类型
     * @param url
     * @return
     */
    public static Dialect checkDialect(String url){
        if(StringUtils.isEmpty(url)){
            return null;
        }
        String dbType = JdbcUtils.getDbType(url,null);
//        if(url.toLowerCase().indexOf("oracle")!=-1){
//            return new OracleDialect();
//        }
        if(dbType.equalsIgnoreCase("mysql")){
            return new MysqlDialect();
        }
        // 添加oracle方言支持
        if(dbType.equalsIgnoreCase("oracle")){
            return new OracleDialect();
        }

        if(dbType.equalsIgnoreCase("h2")){
            //jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1
            if(url.toLowerCase().indexOf("mysql")>0){
                return new MysqlDialect();
            }else if(url.toLowerCase().indexOf("oracle")>0){
                return new OracleDialect();
            }
        }
//        if(url.toLowerCase().indexOf("sqlserver")!=-1){
//            return new SqlServerDialect();
//        }
        return null;
    }

    /**
     * 替换sql中的order by
     * @param sql
     * @return
     */
    public static String replaceFormatSqlOrderBy(String sql) {
        sql = sql.replaceAll("(\\s)+", " ");
        int index = sql.toLowerCase().lastIndexOf("order by");
        if (index > sql.toLowerCase().lastIndexOf(")")) {
            String sql1 = sql.substring(0, index);
            String sql2 = sql.substring(index);
            sql2 = sql2.replaceAll("[oO][rR][dD][eE][rR] [bB][yY] [`\u4e00-\u9fa5a-zA-Z0-9_.]+((\\s)+(([dD][eE][sS][cC])|([aA][sS][cC])))?(( )*,( )*[\u4e00-\u9fa5a-zA-Z0-9_.]+(( )+(([dD][eE][sS][cC])|([aA][sS][cC])))?)*", "");
            return sql1 + sql2;
        }
        return sql;
    }


}
