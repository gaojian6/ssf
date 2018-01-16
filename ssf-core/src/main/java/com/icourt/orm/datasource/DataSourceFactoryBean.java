/*
 * @date 2016年12月12日 15:10
 */
package com.icourt.orm.datasource;

import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;

/**
 * @author june
 */
public class DataSourceFactoryBean implements FactoryBean<DataSource> {

    private DataSource dataSource;

    @Override
    public DataSource getObject() throws Exception {
        return dataSource;
    }

    @Override
    public Class<?> getObjectType() {
        if(dataSource!=null){
            return dataSource.getClass();
        }
        return DataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
