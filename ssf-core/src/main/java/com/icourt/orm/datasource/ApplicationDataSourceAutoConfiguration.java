/*
 * @date 2016年12月12日 14:32
 */
package com.icourt.orm.datasource;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * @author june
 */
@ConditionalOnClass(DataSource.class)
@Import(DataSourceRegister.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class ApplicationDataSourceAutoConfiguration {



}
