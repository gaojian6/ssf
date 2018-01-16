package com.icourt.common.page.interceptor;

import com.icourt.common.page.Pagination;
import com.icourt.common.page.dialect.Dialect;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;

import javax.xml.bind.PropertyException;
import java.io.Serializable;
import java.util.Properties;

/**
 * <p>
 * 功能： 1. 参数对象转换为Page对象。<br>
 * 2. 配置读取：dialectClass, sqlPattern, pageFieldName
 * </p>
 *
 * @author lan
 * @version 1.0
 */
public abstract class BaseInterceptor implements Interceptor, Serializable {

    private static final long serialVersionUID = 4596430444388728543L;

    protected Log log = LogFactory.getLog(this.getClass());

    protected static final String DELEGATE = "delegate";

    protected static final String MAPPED_STATEMENT = "mappedStatement";

    protected Dialect dialect;

    /**
     * 拦截的ID，在mapper中的id，可以匹配正则
     */
    protected String sqlPattern = "";

    protected static String MAP_PAGE_FIELD = Pagination.MAP_PAGE_FIELD;

    /**
     * 对参数进行转换和检查
     *
     * @param parameterObject 参数对象
     * @return 参数VO
     * @throws NoSuchFieldException 无法找到参数
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static Pagination convertParameter(Object parameterObject, Pagination pagination)
            throws NoSuchFieldException {
        if (parameterObject instanceof Pagination) {
            pagination = (Pagination) parameterObject;
        } else if (parameterObject instanceof java.util.Map) {
            java.util.Map parameterMap = (java.util.Map) parameterObject;
            pagination = (Pagination) parameterMap.get(MAP_PAGE_FIELD);
            if (pagination == null) {
                throw new PersistenceException("分页参数不能为空");
            }
        }
        return pagination;
    }

    /**
     * 设置属性，支持自定义方言类和制定数据库的方式
     * <p>
     * <code>dialectClass</code>,自定义方言类。<br>
     * <code>sqlPattern</code> 需要拦截的SQL ID
     * </p>
     *
     * @param p 属性
     */
    protected void initProperties(Properties p) {
        String dialectClass = p.getProperty("dialectClass");
        if (StringUtils.isEmpty(dialectClass)) {
            try {
                throw new PropertyException("数据库分页方言无法找到!");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        } else {
            Dialect dialect1;
            try {
                Class<Dialect> forName = (Class<Dialect>) Class.forName(dialectClass);
                dialect1 = forName.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("方言实例错误");
            }
            if (dialect1 == null) {
                throw new RuntimeException("方言实例错误");
            }
            dialect = dialect1;
        }

        sqlPattern = p.getProperty("sqlPattern");
        if (StringUtils.isEmpty(sqlPattern)) {
            try {
                throw new PropertyException("sqlPattern property is not found!");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        }

        String mapPageField = p.getProperty("mapPageField");
        if (StringUtils.isNotEmpty(mapPageField)) {
            MAP_PAGE_FIELD = mapPageField;
        }
    }
}
