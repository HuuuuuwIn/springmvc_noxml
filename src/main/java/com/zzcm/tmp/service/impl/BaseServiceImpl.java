package com.zzcm.tmp.service.impl;

import com.zzcm.tmp.dao.BaseDao;
import com.zzcm.tmp.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/24.
 */
public abstract class BaseServiceImpl<T,K extends Serializable> implements BaseService<T,K> {

    @Override
    public T findById(K id) {
        return getBaseDao().findById(id);
    }

    @Override
    @Transactional
    public T saveOrUpdate(T bean) {
        return getBaseDao().saveOrUpdate(bean);
    }

    @Override
    @Transactional
    public boolean deleteById(K id) {
        return getBaseDao().deleteById(id);
    }

    @Override
    @Transactional
    public boolean delete(T bean) {
        return getBaseDao().delete(bean);
    }

    @Override
    @Transactional
    public T update(T bean) {
        return getBaseDao().update(bean);
    }

    @Override
    public List<T> getAll() {
        return getBaseDao().getAll();
    }

    protected abstract BaseDao<T,K> getBaseDao();
}
