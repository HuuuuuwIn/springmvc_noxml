package com.zzcm.tmp.service.impl;

import com.zzcm.tmp.bean.Task;
import com.zzcm.tmp.dao.BaseDao;
import com.zzcm.tmp.dao.TaskDao;
import com.zzcm.tmp.quartz.JobFactory;
import com.zzcm.tmp.quartz.JobSyncFactory;
import com.zzcm.tmp.service.TaskService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
@Component
public class TaskServiceImpl extends BaseServiceImpl<Task,Integer> implements TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);
    /** 调度工厂Bean */
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private TaskDao dao;

    public List<Task> getEnableTasks() {
        return dao.getEnableTasks();
    }

    public void run(int id) {
        Task task = dao.findById(id);
        if(null==task) return;

        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetClass(JobFactory.class);
        bean.setTargetMethod("work");
        bean.setGroup(task.getGroup());
        bean.setName(task.getName());

        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(bean.getObject());
        triggerFactoryBean.setCronExpression(task.getCronTime());

        SchedulerFactoryBean schedulerFactoryBean  = new SchedulerFactoryBean();

        try {
            schedulerFactoryBean.getScheduler().scheduleJob(bean.getObject(),triggerFactoryBean.getObject());
            schedulerFactoryBean.getScheduler().triggerJob(getJobKey(task));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getAll() {
        return dao.getAll();
    }

    public List<String> getRunningTaskNames() {
        List<String> list = new ArrayList<String>();
        try {
            List<JobExecutionContext> exeJobs = scheduler.getCurrentlyExecutingJobs();
            for (JobExecutionContext job : exeJobs) {
                JobDetail jobDetail = job.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                list.add(jobKey.getName());
            }
        } catch (SchedulerException e) {
            LOG.error("getRunningTaskNames failed.",e);
        }
        return list;
    }

    private JobKey getJobKey(Task task){
        return JobKey.jobKey(task.getName(),task.getGroup());
    }

    @Override
    @Transactional
    public boolean deleteTask(int id) {
        Task task = findById(id);
        try {
            scheduler.deleteJob(getJobKey(task));
        } catch (SchedulerException e) {
            LOG.error("deleteTask["+id+"] failed.",e);
            return false;
        }
        return delete(task);
    }

    @Override
    @Transactional
    public boolean startTask(int id) {
        Task task = findById(id);
        try {
            if(createTask(task)){
                task.setStatus(Task.Status.RUN);
                update(task);
                return true;
            }
            return false;
        } catch (Exception e) {
            LOG.error("deleteTask["+id+"] failed.",e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean stopTask(int id) {
        Task task = findById(id);
        try {
            scheduler.deleteJob(getJobKey(task));
            task.setStatus(Task.Status.STOP);
            update(task);
            return true;
        } catch (SchedulerException e) {
            LOG.error("deleteTask["+id+"] failed.",e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean pauseTask(int id) {
        Task task = findById(id);
        try {
            scheduler.pauseJob(getJobKey(task));
            task.setStatus(Task.Status.PAUSE);
            update(task);
            return true;
        } catch (SchedulerException e) {
            LOG.error("pauseTask[" + id + "] failed.", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean resumeTask(int id) {
        Task task = findById(id);
        try {
            scheduler.resumeJob(getJobKey(task));
            task.setStatus(Task.Status.RUN);
            update(task);
            return true;
        } catch (SchedulerException e) {
            LOG.error("resumeTask[" + id + "] failed.", e);
            return false;
        }
    }

    @Override
    @Transactional
    public Task insertTask(Task bean) {
        bean = saveOrUpdate(bean);
        if(null==bean) return null;
        //判断是否启用，启用的话就开启
        if(!bean.getEnable()) return bean;
        if(createTask(bean)) {
            bean.setStatus(Task.Status.RUN);
            update(bean);
            return bean;
        }
        return null;
    }

    private boolean createTask(Task bean){
        //同步或异步
        Class<? extends Job> jobClass = bean.getSync() ? JobSyncFactory.class : JobFactory.class;

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(bean.getName(), bean.getGroup()).build();

        //放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put("jobParam", bean);

        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(bean.getCronTime());

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(bean.getName(), bean.getGroup())
                .withSchedule(scheduleBuilder).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            LOG.error("createTask ["+bean+"]", e);
            return false;
        }
    }

    @Override
    protected BaseDao<Task, Integer> getBaseDao() {
        return dao;
    }
}
