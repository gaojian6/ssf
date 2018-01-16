package com.icourt.quartz;

import com.icourt.core.Page;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;


public class QuartzService implements IQuartzService{

    @Autowired
    private JobBeanDao jobBeanDao;

    public void save(JobBean jobBean) {
//        if (jobBean.getType().equals(JobBean.REMOTE_JOB)) {// 远程定时任务的执行类
//            jobBean.setTriggerName("com.ssf.here.quartz.RemoteQuartzJob");
//        }
        jobBean.setCrTime(new Date());
        jobBeanDao.save(jobBean);
        JobKit.addJob(jobBean);
    }

    public void modify(JobBean jobBean) {
        jobBean.setLastUpdateTime(new Date());
        jobBeanDao.update(jobBean);
        JobKit.updateJob(jobBean);
    }

    public void delete(JobBean jobBean) {
        jobBeanDao.delete(jobBean);
        JobKit.deleteJob(jobBean);
    }

    public boolean pause(Long id, Integer status) {

        Assert.notNull(id, "jobBean id can not be null.");
        Assert.notNull(status == JobBean.RUN_STATUS || status == JobBean.STOP_STATUS, "Illegal status.");

        JobBean jobBean = findById(id);

        if (jobBean == null) {
            return false;
        }

        jobBean.setStatus(status);
        jobBean.setLastUpdateTime(new Date());
        jobBeanDao.update(jobBean,"status", "lastUpdateTime");

        return JobKit.updateJob(jobBean);
    }

    public JobBean findById(Long id) {
        List<JobBean> jobBeanList = jobBeanDao.findByColumns(new String[]{"id"},id);
        if (null == jobBeanList || 0 == jobBeanList.size()) {
            return null;
        }
        return jobBeanList.get(0);
    }

    public JobBean findByName(String name) {
        List<JobBean> jobBeanList = jobBeanDao.findByColumns(new String[]{"name"},name);
        if (null == jobBeanList || 0 == jobBeanList.size()) {
            return null;
        }
        return jobBeanList.get(0);
    }

    public Page<JobBean> pageQuery(Integer pageNo,
                                   Integer pageSize,
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
        Page<JobBean> jobBeanPage = jobBeanDao.pageQuery(pageNo,pageSize,name,triggerCron,instanceName,type,status,triggerName,appName,target,methodName,remark);
        setRunTime(jobBeanPage);
        return jobBeanPage;
    }

    /**
     * 设置上次执行时间和下次执行时间
     *
     * @param page
     */
    private void setRunTime(Page<JobBean> page) {
        if(page!=null) {
            JobBean[] jobs = page.getEntities().toArray(new JobBean[]{});
            for (JobBean jobBean : jobs) {
                CronTrigger cronTrigger = JobKit.getCronTrigger(jobBean.getId());
                if (cronTrigger != null) {
                    jobBean.setLastRunTime(cronTrigger.getPreviousFireTime());
                    jobBean.setNextRunTime(cronTrigger.getNextFireTime());
                }
            }
        }
    }
}
