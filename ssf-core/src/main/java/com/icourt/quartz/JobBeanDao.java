/*
 * @date 2016年12月20日 13:15
 */
package com.icourt.quartz;

import com.icourt.common.StrKit;
import com.icourt.core.Page;
import com.icourt.orm.dao.IApplicationDao;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author june
 */
public class JobBeanDao implements ApplicationContextAware {

    private IApplicationDao applicationDao;

    /**
     * 保存调度
     * @param jobBean jobBean
     * @return jobBean
     */
    public JobBean save(JobBean jobBean){
        Assert.isNull(jobBean.getId());
        return applicationDao.save(jobBean);
    }

    /**
     * 更新调度
     * @param jobBean 调度
     * @param columns 要更新的列
     */
    public void update(JobBean jobBean,String... columns){
        Assert.notNull(jobBean.getId());
        applicationDao.update(jobBean,columns);
    }

    /**
     * 删除某个调度
     * @param jobBean 调度
     */
    public void delete(JobBean jobBean){
        Assert.notNull(jobBean.getId());
        applicationDao.delete(jobBean);
    }

    /**
     * 根据列查找
     * @param whereColumns 要查找的列
     * @param params 参数
     * @return 调度集合
     */
    public List<JobBean> findByColumns(String[] whereColumns,Object... params){
        return applicationDao.findByColumns(JobBean.class,whereColumns,params);
    }

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
    public Page<JobBean> pageQuery(Integer pageNo, Integer pageSize,
                                   String name,
                                   String triggerCron,
                                   String instanceName,
                                   Integer type,
                                   Integer status,
                                   String triggerName,
                                   String appName,
                                   String target,
                                   String methodName,
                                   String remark) {

        Map<String, Object> param = new HashMap<>();

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ");
        sqlBuilder.append(JobBean.TABLENAME);

        sqlBuilder.append(" WHERE 1 = 1");

        if (StrKit.isNotEmpty(name)) {
            sqlBuilder.append(" AND name = :name");
            param.put("name", name);
        }
        if (StrKit.isNotEmpty(triggerCron)) {
            sqlBuilder.append(" AND triggerCron = :triggerCron");
            param.put("triggerCron", triggerCron);
        }
        if (StrKit.isNotEmpty(instanceName)) {
            sqlBuilder.append(" AND instanceName = :instanceName");
            param.put("instanceName", instanceName);
        }
        if (null != type) {
            sqlBuilder.append(" AND type = :type");
            param.put("type", type);
        }
        if (null != status) {
            sqlBuilder.append(" AND status = :status");
            param.put("status", status);
        }
        if (StrKit.isNotEmpty(triggerName)) {
            sqlBuilder.append(" AND triggerName = :triggerName");
            param.put("triggerName", triggerName);
        }
        if (StrKit.isNotEmpty(appName)) {
            sqlBuilder.append(" AND appName = :appName");
            param.put("appName", appName);
        }
        if (StrKit.isNotEmpty(target)) {
            sqlBuilder.append(" AND target = :target");
            param.put("target", target);
        }
        if (StrKit.isNotEmpty(methodName)) {
            sqlBuilder.append(" AND methodName = :methodName");
            param.put("methodName", methodName);
        }

        if (StrKit.isNotEmpty(remark)) {
            sqlBuilder.append(" AND remark LIKE :remark");
            param.put("remark", "%" + remark + "%");
        }
        return applicationDao.findPage(
                JobBean.class,
                pageNo,
                pageSize,
                param,
                sqlBuilder.toString());
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String dsName = null;
        ResourceBundle rb = ResourceBundle.getBundle("quartz");
        String keyName = "ssf.quartz.dataSource.name";
        if(rb.containsKey(keyName)){
            dsName = rb.getString(keyName);
        }
        if(StrKit.isNotEmpty(dsName)){
            applicationDao = applicationContext.getBean(dsName,IApplicationDao.class);
        }else{
            applicationDao = applicationContext.getBean(IApplicationDao.class);
        }
    }
}
