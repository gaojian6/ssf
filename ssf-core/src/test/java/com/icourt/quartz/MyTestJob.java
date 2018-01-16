package com.icourt.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author june
 */
public class MyTestJob implements IApplicationJob{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("我是定时任务...");
    }

}