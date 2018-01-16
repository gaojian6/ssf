package com.icourt.quartz;

import com.alibaba.fastjson.JSON;
import com.icourt.common.BeanKit;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ResourceBundle;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author june
 */
public class JobKit {

    private static Logger logger = LoggerFactory.getLogger(JobKit.class);
    //缓存所有job
//    private static Map<Long,JobBean> jobMap = new HashMap<Long,JobBean>();
    //key前缀
    private static String keyPrefix = "ssf_";

    private static Scheduler scheduler = null;

    /**
     * 初始化quartz
     */
    public static void init(){
        //启动quartz
        start();
        //加载已配置的定时任务
        loadJob();
    }
    /**
     * 启动
     */
    private static void start(){
        try {
            SchedulerFactory sf = new StdSchedulerFactory();
            scheduler = sf.getScheduler();
            if(scheduler.isStarted()){
                scheduler.shutdown(true);
            }
            scheduler.start();
        } catch (SchedulerException e) {
            throw new QuartzException(e);
        }
    }

    /**
     * 从数据库加载所有定时任务
     */
    private static void loadJob() {
        ResourceBundle rb = ResourceBundle.getBundle("quartz");
        String keyName = "org.quartz.scheduler.instanceName";
        String instanceName = rb.getString(keyName);
        //查找实例名称
        List<JobBean> jobBeanList = BeanKit.getBean(JobBeanDao.class).findByColumns(new String[]{"instanceName"},instanceName);
        if(jobBeanList!=null && jobBeanList.size()>0){
            for(JobBean jobBean : jobBeanList){
                addJob(jobBean);
            }
        }
    }

    /**
     * 新增调度
     * @param jobBean
     */
    public static boolean addJob(JobBean jobBean){
        try {
            if(!CronExpression.isValidExpression(jobBean.getTriggerCron())){
                throw new QuartzException("表达式不合法");
            }
            if(jobBean.getStatus()==JobBean.STOP_STATUS){
                return false;
            }
            if(jobBean.getId()==null){
                throw new QuartzException("jobBean id不能为空");
            }
            String jobKeyId = keyPrefix+jobBean.getId();
            JobKey jobkey = new JobKey(jobKeyId, jobKeyId);
            //任务已存在
            if(scheduler.checkExists(jobkey)){
                logger.info("定时任务已存在..."+jobBean);
                return false;
            }
            //新建调度
            CronTrigger trigger = newTrigger()
                    .withIdentity(jobKeyId, jobKeyId)
                    .withSchedule(cronSchedule(jobBean.getTriggerCron())
                            .withMisfireHandlingInstructionDoNothing())//不触发立即执行,等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
                    .build();
                    /*withMisfireHandlingInstructionDoNothing
                    ——不触发立即执行
                    ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行

                    withMisfireHandlingInstructionIgnoreMisfires
                    ——以错过的第一个频率时间立刻开始执行
                    ——重做错过的所有频率周期后
                    ——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行

                    withMisfireHandlingInstructionFireAndProceed
                    ——以当前时间为触发频率立刻触发一次执行
                    ——然后按照Cron频率依次执行*/

            JobBuilder jobBuilder = newJob((Class) Class.forName(jobBean.getTriggerName()).newInstance().getClass());
            jobBuilder.withIdentity(jobKeyId, jobKeyId);
            //任务描述
            jobBuilder.withDescription(jobBean.getName());
            //把jobBean传递给定时任务
            jobBuilder.usingJobData("jobBean", JSON.toJSONString(jobBean));

            JobDetail job = jobBuilder.build();
            //绑定
            scheduler.scheduleJob(job, trigger);
            //是否立即执行一次定时任务
//            scheduler.triggerJob(jobkey);
            logger.info("add task success,"+jobBean.toString());
            return true;
        }catch (Exception e){
            throw new QuartzException(e);
        }
    }

    /**
     * 更新调度,如果是远程的定时任务,修改了远程相关参数,需要暂停后再启用
     * @param jobBean 调度
     * @return
     */
    public static boolean updateJob(JobBean jobBean){
        if(jobBean==null || jobBean.getId()==null){
            return false;
        }
        if(!CronExpression.isValidExpression(jobBean.getTriggerCron())){
            throw new QuartzException("表达式不合法");
        }
        try{
            //如果是暂停
            if(jobBean.getStatus()==JobBean.STOP_STATUS){
                return deleteJob(jobBean);
            }
            String jobKeyId = keyPrefix+jobBean.getId();
            JobKey jobkey = new JobKey(jobKeyId, jobKeyId);
            TriggerKey triggerKey = TriggerKey.triggerKey(jobKeyId,jobKeyId);
            //任务不存在
            if(!scheduler.checkExists(jobkey)){
                return addJob(jobBean);
            }
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(cronSchedule(jobBean.getTriggerCron())
                            .withMisfireHandlingInstructionDoNothing()).build();
            scheduler.rescheduleJob(triggerKey,trigger);
            return true;
        }catch (Exception e){
            throw new QuartzException(e);
        }
    }

    /**
     * 删除调度
     * @param jobBean
     * @return
     */
    public static boolean deleteJob(JobBean jobBean){
        if(jobBean==null || jobBean.getId()==null){
            return false;
        }
        try{
            String jobKeyId = keyPrefix+jobBean.getId();
            JobKey jobkey = new JobKey(jobKeyId, jobKeyId);
            //任务不存在
            if(!scheduler.checkExists(jobkey)){
                return false;
            }
            scheduler.deleteJob(jobkey);
            return true;
        }catch (Exception e){
            throw new QuartzException(e);
        }
    }

    /**
     * 获取job的CronTrigger
     * @param jobBeanId
     * @return
     */
    public static CronTrigger getCronTrigger(Long jobBeanId){
        try {
            if(jobBeanId==null){
                return null;
            }
            String jobKeyId = keyPrefix+jobBeanId;
            JobKey jobkey = new JobKey(jobKeyId, jobKeyId);
            //任务不存在
            if(!scheduler.checkExists(jobkey)){
                return null;
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(jobKeyId,jobKeyId);
            return (CronTrigger) scheduler.getTrigger(triggerKey);
        }catch (Exception e){
            throw new QuartzException(e);
        }
    }

    /**
     * 关闭调度
     */
    public static void shutDown(){
        try {
            scheduler.shutdown(true);
        }catch (Exception e){
            throw new QuartzException(e);
        }
    }
}
