/*
 * @date 2016年11月21日 09:21
 */
package com.icourt.core;

import com.icourt.common.IpKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/**
 * 打印启动日志
 * @author june
 */
public class StartLogCommandLineRunner extends ApplicationCommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartLogCommandLineRunner.class);

    private final Environment env;

    public StartLogCommandLineRunner(Environment env){
        this.env = env;
    }

    @Override
    protected int getRunnerOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void run(String... args) throws Exception {
        String port = env.getProperty("server.port","8080");
        String contextPath = env.getProperty("server.context-path","/");
        String applicationName = env.getProperty("spring.application.name","");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application {} is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}{}\n\t" +
                        "External: \thttp://{}:{}{}\n----------------------------------------------------------",
                applicationName,
                port,
                contextPath,
                IpKit.getLocalIp(),
                port,
                contextPath);
    }
}
