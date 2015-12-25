package com.zzcm.tmp.service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/24.
 */
public interface BaseService<T,K extends Serializable> {
    public T findById(K id);

    public T saveOrUpdate(T bean);

    public boolean deleteById(K id);

    public boolean delete(T bean);

    public T update(T bean);

    public List<T> getAll();
}
