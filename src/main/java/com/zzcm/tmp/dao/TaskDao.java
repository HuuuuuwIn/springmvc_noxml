package com.zzcm.tmp.dao;


import com.zzcm.tmp.bean.Task;

import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
public interface TaskDao extends BaseDao<Task,Integer>{
    public List<Task> getEnableTasks();
}
