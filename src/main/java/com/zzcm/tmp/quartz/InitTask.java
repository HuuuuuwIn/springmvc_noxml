package com.zzcm.tmp.quartz;

import com.zzcm.tmp.bean.Task;
import com.zzcm.tmp.service.TaskService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Administrator on 2015/12/23.
 */
@Service
public class InitTask{
    private static final Logger LOG = LoggerFactory.getLogger(InitTask.class);
    @Autowired
    private TaskService taskService;
    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void init(){
        List<Task> tasks = taskService.getEnableTasks();
        for (Task task : tasks) {
            try {
                Class<? extends Job> jobClass = task.getSync() ? JobSyncFactory.class : JobFactory.class;
                JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(task.getName(), task.getGroup()).build();
                jobDetail.getJobDataMap().put("jobParam",task);
                // 表达式调度构建器
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronTime());
                // 按新的表达式构建一个新的trigger
                CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(task.getName(),task.getGroup()).withSchedule(cronScheduleBuilder).build();
                scheduler.scheduleJob(jobDetail,cronTrigger);
                //JobKey jobKey = JobKey.jobKey(task.getName(),task.getGroup());
                //scheduler.triggerJob(jobKey);
                task.setStatus(Task.Status.RUN);
                taskService.update(task);
            } catch (SchedulerException e) {
                LOG.error("init task["+task.getId()+"] failed.",e);
                e.printStackTrace();
            }
        }
    }
}
