package com.zzcm.tmp.dao.impl;

import com.zzcm.tmp.bean.Task;
import com.zzcm.tmp.dao.TaskDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
@Repository
public class TaskDaoImpl extends BaseDaoImpl<Task,Integer> implements TaskDao {

    @Override
    public List<Task> getEnableTasks() {
        try {
            Query query = em.createQuery("from Task where enable = true");
            return query.getResultList();
        }catch (Exception e){
            LOGGER.error("getEnableTasks failed.",e);
        }
        return null;
    }
}
