/*
 * @date 2016年12月12日 14:26
 */
package com.icourt.orm.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.icourt.common.SafeKit;
import org.apache.log4j.Logger;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author june
 */

public class DataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;
    //所有数据源
    public static final Map<String,DataSource> allDataSourceMap = new HashMap<>();
    //数据源是否只读
    public static final Map<String,Boolean> readOnlyMap = new HashMap<>();
    private static final Logger logger = Logger.getLogger(DataSourceRegister.class);
    public static final String prefix = "ssf.datasource";
    // 如配置文件中未指定数据源类型，使用该默认值
    private static final Object DEFAULT_DATASOURCE_TYPE = "com.alibaba.druid.pool.DruidDataSource";
    //所有数据源id
    public static final List<String> allDataSourceNames = new ArrayList<>();
    //默认数据源名称
    public static final String defaultDataSourceName = "master";



    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        initDataSourceName();
        initDataSources();
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        for (String dataSourceName : allDataSourceNames) {
            //注册dataSource
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(DataSourceFactoryBean.class);
            beanDefinition.setSynthetic(true);
            if(dataSourceName.equals(defaultDataSourceName)){
                beanDefinition.setPrimary(true);
            }
            MutablePropertyValues mpv = beanDefinition.getPropertyValues();
            mpv.addPropertyValue("dataSource", allDataSourceMap.get(dataSourceName));
            registry.registerBeanDefinition(dataSourceName+"DataSource", beanDefinition);
            if(!readOnlyMap.get(dataSourceName)) {
                //注册事务
                GenericBeanDefinition txBeanDefinition = new GenericBeanDefinition();
                txBeanDefinition.setBeanClass(DataSourceTransactionManager.class);
                txBeanDefinition.setSynthetic(true);
                if(dataSourceName.equals(defaultDataSourceName)){
                    txBeanDefinition.setPrimary(true);
                }
                txBeanDefinition.getPropertyValues().addPropertyValue("dataSource", allDataSourceMap.get(dataSourceName));
                registry.registerBeanDefinition(dataSourceName + "Tx", txBeanDefinition);
            }
        }

    }

    /**
     * 获取所有数据源名称
     */
    private void initDataSourceName(){
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment,prefix);
        Map<String, Object> rpr = propertyResolver.getSubProperties(".");
        if(rpr!=null && !rpr.isEmpty()){
            for (String key : rpr.keySet()) {
                String dataSourceName = key.split("\\.")[0];
                if(!allDataSourceNames.contains(dataSourceName)) {
                    allDataSourceNames.add(dataSourceName);
                    logger.info("add dataSourceName:"+dataSourceName);
                }
            }
        }
    }

    /**
     * 初始化数据源
     */
    private void initDataSources(){
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment,prefix);
        for (String dsId : allDataSourceNames) {
            Map<String, Object> dsMap = propertyResolver.getSubProperties("."+dsId + ".");
            DataSource dataSource = buildDataSource(dsMap);
            if(dataSource instanceof DruidDataSource){
                try {
                    DruidDataSource druidDataSource = ((DruidDataSource) dataSource);
                    druidDataSource.init();
                    logger.info("init DruidDataSource Success,url:"+druidDataSource.getUrl()+",username:"+druidDataSource.getUsername());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            allDataSourceMap.put(dsId,dataSource);
            //是否只读
            String readOnlyStr = environment.getProperty(prefix+"."+dsId + ".readOnly","false");
            readOnlyMap.put(dsId, SafeKit.getBoolean(readOnlyStr));
        }
    }

    /**
     * 构建dataSource
     * @param dsMap 配置信息
     * @return DataSource
     */
    @SuppressWarnings("unchecked")
    private DataSource buildDataSource(Map<String, Object> dsMap){
        //数据源类型
        Object type = dsMap.get("type");
        if (type == null){
            type = DEFAULT_DATASOURCE_TYPE;
        }
        try {
            Class<? extends DataSource> dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
            DataSourceBuilder factory = DataSourceBuilder.create().type(dataSourceType);
            DataSource dataSource = factory.build();
            RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
            dataBinder.setConversionService(new DefaultConversionService());
            dataBinder.setIgnoreNestedProperties(false);
            dataBinder.setIgnoreInvalidFields(false);
            dataBinder.setIgnoreUnknownFields(true);
            dataBinder.bind(new MutablePropertyValues(dsMap));
            return dataSource;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
