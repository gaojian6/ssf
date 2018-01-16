package com.icourt.quartz;

import com.icourt.common.StrKit;
import com.icourt.core.Page;
import com.icourt.core.Result;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RequestMapping("quartzs")
@ResponseBody
public class QuartzController{

    @Autowired
    private IQuartzService quartzService;

    @Autowired
    private IQuartzAuthService quartzAuthService;

    @GetMapping
    public Result list(@RequestParam Integer pageNo,
                       @RequestParam Integer pageSize,
                       String name,
                       String triggerCron,
                       String instanceName,
                       Integer type,
                       String triggerName,
                       String appName,
                       String target,
                       String methodName,
                       Integer status,
                       String remark){

        if (!quartzAuthService.hasRight()){
            return Result.fail("access-denied");
        }
        if (pageNo<0){
            pageNo = 1;
        }
        if (pageSize<=0 || pageSize>10000){
            pageSize = 20;
        }
        Page<JobBean> page = quartzService.pageQuery(pageNo,pageSize,name,triggerCron,instanceName,type,status,triggerName,appName,target,methodName,remark);
        Result result = Result.success();
        result.setData(page);
        return result;
    }

    @PostMapping
    public Result add(@RequestParam String name,
                      @RequestParam String triggerCron,
                      @RequestParam String instanceName,
                      @RequestParam Integer type,
                      @RequestParam String triggerName,
                      @RequestParam String appName,
                      @RequestParam String target,
                      @RequestParam String methodName,
                      @RequestParam Integer status,
                      String remark) {
        if (!quartzAuthService.hasRight()){
            return Result.fail("access-denied");
        }
        if (null != quartzService.findByName(name)){
            return Result.fail("name-occupied");
        }
        if(!CronExpression.isValidExpression(triggerCron)){
            return Result.fail("invalid-format-triggerCron");
        }
        if (null == type || (type != JobBean.LOCAL_JOB && type != JobBean.REMOTE_JOB)) {
            return Result.fail("invalid-type");
        }
        if (type == JobBean.LOCAL_JOB && StrKit.isEmpty(triggerName)){
            return Result.fail("invalid-triggerName");
        }
        if (type == JobBean.LOCAL_JOB && StrKit.isNotEmpty(triggerName)){
            try{
                Class.forName(triggerName).newInstance().getClass();
            }catch (Exception e){
                return Result.fail("triggerName-class-not-found");
            }
        }
        if (type == JobBean.REMOTE_JOB && StrKit.isEmpty(appName)){
            return Result.fail("invalid-appName");
        }
        if (type == JobBean.REMOTE_JOB && StrKit.isEmpty(target)){
            return Result.fail("invalid-target");
        }
        if (type == JobBean.REMOTE_JOB && StrKit.isEmpty(methodName)){
            return Result.fail("invalid-methodName");
        }
        if (null == status || (status != JobBean.RUN_STATUS && status != JobBean.STOP_STATUS)){
            return Result.fail("invalid-status");
        }
        JobBean jobBean = new JobBean();
        jobBean.setName(name);
        jobBean.setTriggerCron(triggerCron);
        jobBean.setInstanceName(instanceName);
        jobBean.setType(type);
        jobBean.setTriggerName(triggerName);
        jobBean.setAppName(appName);
        jobBean.setTarget(target);
        jobBean.setMethodName(methodName);
        jobBean.setStatus(status);
        jobBean.setRemark(remark);
        jobBean.setCrUser("system");
        jobBean.setCrTime(new Date());
        quartzService.save(jobBean);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result edit(@PathVariable Long id,
                       @RequestParam String triggerCron,
                       @RequestParam String instanceName,
                       @RequestParam Integer type,
                       @RequestParam String triggerName,
                       @RequestParam String appName,
                       @RequestParam String target,
                       @RequestParam String methodName,
                       String remark) {
        if (!quartzAuthService.hasRight()){
            return Result.fail("access-denied");
        }
        JobBean jobBean = quartzService.findById(id);
        if (null == jobBean){
            return Result.fail("JobBean-not-found");
        }
        if(!CronExpression.isValidExpression(triggerCron)){
            return Result.fail("invalid-format-triggerCron");
        }
        if (null == type || (type != JobBean.LOCAL_JOB && type != JobBean.REMOTE_JOB)) {
            return Result.fail("invalid-type");
        }

        if (type == JobBean.LOCAL_JOB && StrKit.isEmpty(triggerName)){
            return Result.fail("invalid-triggerName");
        }
        if (type == JobBean.LOCAL_JOB && StrKit.isNotEmpty(triggerName)){
            try{
                Class.forName(triggerName).newInstance().getClass();
            }catch (Exception e){
                return Result.fail("triggerName-class-not-found");
            }
        }
        if (type == JobBean.REMOTE_JOB && StrKit.isEmpty(appName)){
            return Result.fail("invalid-appName");
        }
        if (type == JobBean.REMOTE_JOB && StrKit.isEmpty(target)){
            return Result.fail("invalid-target");
        }
        if (type == JobBean.REMOTE_JOB && StrKit.isEmpty(methodName)){
            return Result.fail("invalid-methodName");
        }

        jobBean.setTriggerCron(triggerCron);
        jobBean.setType(type);
        jobBean.setTriggerName(triggerName);
        jobBean.setInstanceName(instanceName);
        jobBean.setAppName(appName);
        jobBean.setTarget(target);
        jobBean.setMethodName(methodName);
        jobBean.setRemark(remark);
        jobBean.setLastUpdateTime(new Date());

        quartzService.modify(jobBean);
        return Result.success();

    }

    @PutMapping("/{id}/enable")
    public Result enable(@PathVariable Long id,
                         @RequestParam Integer status) {
        if (!quartzAuthService.hasRight()){
            return Result.fail("access-denied");
        }
        if (null == id || 0 > id){
            return Result.fail("invalid-id");
        }
        if (null == quartzService.findById(id)){
            return Result.fail("JobBean-not-found");
        }
        if (null == status || (status != 1 && status !=2)){
            return Result.fail("invalid-status");
        }
        quartzService.pause(id,status);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        if (!quartzAuthService.hasRight()){
            return Result.fail("access-denied");
        }
        JobBean jobBean = quartzService.findById(id);
        if (null == jobBean){
            return Result.fail("JobBean-not-found");
        }
        quartzService.delete(jobBean);
        return Result.success();
    }
}
