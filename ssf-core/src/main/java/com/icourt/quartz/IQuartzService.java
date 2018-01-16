/*
 * @date 2016年12月20日 13:18
 */
package com.icourt.quartz;

import com.icourt.core.Page;

/**
 * @author june
 */
public interface IQuartzService {

    /**
     * 新建调度
     * @param jobBean job
     */
    void save(JobBean jobBean);

    /**
     * 修改调度
     * @param jobBean job
     */
    void modify(JobBean jobBean);

    /**
     * 删除调度
     * @param jobBean job
     */
    void delete(JobBean jobBean);

    /**
     * 暂停或启用调度
     * @param id jobId
     * @param status 状态
     * @return true 成功 ，false 失败
     */
    boolean pause(Long id, Integer status);

    /**
     * 根据jobId查找job
     * @param id jobId
     * @return job
     */
    JobBean findById(Long id);

    /**
     * 根据名称查找job
     * @param name 名称
     * @return job
     */
    JobBean findByName(String name);

    /**
     * 分页查找job
     * @param pageNo 当前页
     * @param pageSize 分页大小
     * @param name 名称
     * @param triggerCron 表达式
     * @param instanceName 实例名称
     * @param type  类型
     * @param status 状态
     * @param triggerName triggerName
     * @param appName appName
     * @param target 目标类
     * @param methodName 目标方法
     * @param remark 备注
     * @return jobs
     */
    Page<JobBean> pageQuery(Integer pageNo, Integer pageSize,
                            String name,
                            String triggerCron,
                            String instanceName,
                            Integer type,
                            Integer status,
                            String triggerName,
                            String appName,
                            String target,
                            String methodName,
                            String remark);



}
