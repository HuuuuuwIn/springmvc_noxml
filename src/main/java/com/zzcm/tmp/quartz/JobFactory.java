package com.zzcm.tmp.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务工厂类,非同步
 */
@DisallowConcurrentExecution
public class JobFactory extends JobSyncFactory {

    /* 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(JobFactory.class);

}
