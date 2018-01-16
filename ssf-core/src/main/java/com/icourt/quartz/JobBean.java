package com.icourt.quartz;


import com.icourt.orm.dao.annotation.TableInfo;
import com.icourt.orm.dao.annotation.Transient;

import java.util.Date;

/**
 * 定时任务管理
 * @author june
 */
@TableInfo(tableName = JobBean.TABLENAME)
public class JobBean{

    @Transient
    public static final String TABLENAME = "ssf_quartz_job_config";

    @Transient
    public static final int RUN_STATUS = 1;//运行状态
    @Transient
    public static final int STOP_STATUS = 2;//暂停状态
    @Transient
    public static final int LOCAL_JOB = 1;//本地定时任务
    @Transient
    public static final int REMOTE_JOB = 2;//远程定时任务

    private Long id;

    private String name;//名称

    private String triggerCron;//时间表达式

    private String triggerName;//执行调度的类全路径

    @Transient
    private Date lastRunTime;//上次执行时间

    @Transient
    private Date nextRunTime;//下次执行时间

    private Date lastUpdateTime;//最后更新时间

    private Date crTime;//创建时间

    private String crUser;//创建人

    private String remark;//备注

    private Integer status;//状态1为运行,2为暂停

    private Integer type;//任务类型,1为本机,2为远程

    private String appName;//所属应用

    private String target;//远程定时任务执行者目标类

    private String methodName;//远程定时任务执行者目标方法

    private String instanceName;//实例名称 和配置文件中org.quartz.scheduler.instanceName一致

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Date getCrTime() {
        return crTime;
    }

    public void setCrTime(Date crTime) {
        this.crTime = crTime;
    }

    public String getCrUser() {
        return crUser;
    }

    public void setCrUser(String crUser) {
        this.crUser = crUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(Date lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(Date nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static String getTABLENAME() {
        return TABLENAME;
    }

    public String getTriggerCron() {
        return triggerCron;
    }

    public void setTriggerCron(String triggerCron) {
        this.triggerCron = triggerCron;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "JobBean{" +
                "appName='" + appName + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status + '\'' +
                ", triggerCron='" + triggerCron + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", type=" + type  + '\'' +
                ", target=" + target + '\'' +
                ", methodName=" + methodName + '\'' +
                '}';
    }
}
