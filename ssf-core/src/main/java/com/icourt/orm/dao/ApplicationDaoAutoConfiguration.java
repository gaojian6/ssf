/*
 * @date 2016年10月22日 15:54
 */
package com.icourt.orm.dao;

import com.icourt.orm.datasource.ApplicationDataSourceAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 自动配置dao
 * @author june
 */
@ConditionalOnClass(DataSource.class)
@Import(ApplicationDaoRegister.class)
@AutoConfigureAfter(ApplicationDataSourceAutoConfiguration.class)
public class ApplicationDaoAutoConfiguration implements ApplicationContextAware {

    private ListableBeanFactory beanFactory;

    private Logger logger = LoggerFactory.getLogger(ApplicationDaoAutoConfiguration.class);

    @Autowired(required = false)
    private CacheProvider cacheProvider;

    @Bean
    public SystemInterpreter systemInterpreter(){
        return new SystemInterpreter();
    }

    @Bean
    public Statement jdbcStatement(){
        //获取所有解释器
        Map<String, Interpreter> map = beanFactory.getBeansOfType(Interpreter.class);
        List<Interpreter> interpreters = new ArrayList<>(map.values());
        //排序
        interpreters.sort(Comparator.comparingInt(Ordered::getOrder));
        //添加系统默认解释器
        interpreters.set(0,systemInterpreter());
        return new JdbcStatement(interpreters);
    }

    @Bean
    public Statement statement(){
        Statement statement = jdbcStatement();
        if(cacheProvider!=null){
            statement = new CachedStatement(cacheProvider,statement);
            logger.info("Dao启用缓存cacheProvider:"+cacheProvider.getClass().getName());
        }
        return statement;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanFactory = applicationContext;
    }
}
