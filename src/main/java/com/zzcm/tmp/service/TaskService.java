package com.zzcm.tmp.service;


import com.zzcm.tmp.bean.Task;

import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
public interface TaskService extends BaseService<Task,Integer>{

    public List<Task> getEnableTasks();

    public List<String> getRunningTaskNames();

    public void run(int id);

    public boolean deleteTask(int id);

    public boolean pauseTask(int id);

    public boolean resumeTask(int id);

    public boolean stopTask(int id);

    public boolean startTask(int id);

    public Task insertTask(Task bean);
}
