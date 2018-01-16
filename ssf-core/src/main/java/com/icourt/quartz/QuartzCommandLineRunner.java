package com.icourt.quartz;

import com.icourt.core.ApplicationCommandLineRunner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

/**
 * 定时任务初始化
 * @author june
 */
public class QuartzCommandLineRunner extends ApplicationCommandLineRunner {

    @Value("${ssf.quartz.runner.order:10}")
    private int quartzRunnerOrder;

    private static final Logger logger = Logger.getLogger(QuartzCommandLineRunner.class);

    @Override
    public void run(String... args) throws Exception {
        JobKit.init();
        logger.info("ssf quartz init success");
    }

    @Override
    public void destroy() throws Exception {
        JobKit.shutDown();
    }

    @Override
    protected int getRunnerOrder() {
        return quartzRunnerOrder;
    }
}
