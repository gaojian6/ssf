/*
 * @date 2016年12月12日 16:12
 */
package com.icourt.orm.dao;

import com.icourt.orm.dao.dialect.Dialect;
import com.icourt.orm.dao.utils.SqlKit;
import com.icourt.orm.datasource.DataSourceRegister;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author june
 */
public class ApplicationDaoRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        for (int i = 0; i < DataSourceRegister.allDataSourceNames.size(); i++) {
            String dataSourceName = DataSourceRegister.allDataSourceNames.get(i);
            //注册dataSource
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(ApplicationDao.class);
            beanDefinition.setSynthetic(true);
            if(dataSourceName.equals(DataSourceRegister.defaultDataSourceName)){
                beanDefinition.setPrimary(true);
            }
            MutablePropertyValues mpv = beanDefinition.getPropertyValues();
            mpv.addPropertyValue("dsName", dataSourceName);
            mpv.addPropertyValue("dataSource", new RuntimeBeanReference(dataSourceName+"DataSource"));
            mpv.addPropertyValue("dialect", getDialect(dataSourceName));
            mpv.addPropertyValue("statement",new RuntimeBeanReference("statement"));
            registry.registerBeanDefinition(dataSourceName, beanDefinition);
        }
    }

    /**
     * 获取当前方言
     * @return Dialect
     */
    private Dialect getDialect(String dsName){
        String urlPropKey = DataSourceRegister.prefix + "." + dsName + ".url";
        String url = environment.getProperty(urlPropKey);
        return SqlKit.checkDialect(url);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
