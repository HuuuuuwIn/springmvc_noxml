package com.zzcm.tmp.quartz;

import com.zzcm.tmp.bean.Task;
import com.zzcm.tmp.util.SFTPUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 同步的任务工厂类
 *
 * Created by liyd on 12/19/14.
 */
public class JobSyncFactory implements Job {

    /* 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(JobSyncFactory.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        if(LOG.isErrorEnabled()) LOG.debug("Job execute start.");
        Task task = (Task) context.getMergedJobDataMap().get("jobParam");
        System.out.println("Name:"+task.getName());
        SFTPUtil.downloadGZip(task.getFrom().getIp(), task.getFrom().getUser(), task.getFrom().getPwd(), task.getFromDir(), task.getFileName(), task.getToDir());
        //SFTPUtil.download("192.168.0.180","root","zzcm2014","/opt","cid.jar","D:/test/aaaa.jar");
        //SFTPUtil.downloadGZip("192.168.0.239", "root", "zzcm2014", "/home/iadlogs/iad/10.10.28.105", "iad-info.log.2015-12-04", "D:/test/");
        if(LOG.isErrorEnabled()) LOG.debug("Job execute finish.");
    }
}
