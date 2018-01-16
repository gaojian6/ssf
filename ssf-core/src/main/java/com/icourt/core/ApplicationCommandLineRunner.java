/*
 * @date 2016年11月07日 10:26
 */
package com.icourt.core;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;

/**
 *  CommandLineRunner 需要指定顺序
 *
 * @author june
 */
public abstract class ApplicationCommandLineRunner implements CommandLineRunner, Ordered, DisposableBean {

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public int getOrder() {
        int order = getRunnerOrder();
        logger.info("order:" + order);
        return order;
    }

    protected abstract int getRunnerOrder();

}
