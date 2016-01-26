package com.zzcm.tmp.dao.impl;

import com.zzcm.tmp.dao.BaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
public abstract class BaseDaoImpl<T,K extends Serializable> implements BaseDao<T,K> {
    protected final Logger LOGGER = LoggerFactory.getLogger(getBeanClass());

    @PersistenceContext
    protected EntityManager em;

    protected Class<T> beanClass;

    @Override
    public T findById(K id) {
        try {
            return em.find(getBeanClass(),id);
        }catch (Exception e){
            LOGGER.error("find bean by id["+id+"] failed.",e);
        }
        return null;
    }

    @Override
    public T saveOrUpdate(T bean) {
        try {
            em.persist(bean);
            return bean ;
        } catch (Exception e) {
            LOGGER.error("saveOrUpdate bean["+bean+"] failed.",e);
        }
        return null;
    }

    @Override
    public boolean deleteById(K id) {
        try {
            T bean = findById(id);
            if(null==bean) return false;
            em.remove(bean);
            return true ;
        } catch (Exception e) {
            LOGGER.error("delete bean by id["+id+"] failed.",e);
        }
        return false ;
    }

    @Override
    public boolean delete(T bean) {
        try {
            em.remove(bean);
            return true ;
        } catch (Exception e) {
            LOGGER.error("delete bean["+bean+"] failed.",e);
        }
        return false ;
    }

    @Override
    public T update(T bean) {
        try {
            return em.merge(bean);
        } catch (Exception e) {
            LOGGER.error("update bean["+bean+"] failed.",e);
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        try {
            Query query = em.createQuery("from "+getBeanClass().getSimpleName());
            return query.getResultList();
        }catch (Exception e){
            LOGGER.error("getEnableTasks failed.",e);
        }
        return null;
    }
    
    public int batchInsert(List<T> beans) {
        if(null==beans || beans.isEmpty()) return 0;
        for (int i = 0; i < beans.size(); i++) {
            em.persist(beans.get(i));
            if (i % 50 == 0) {
                em.flush();
                em.clear();
            }
        }

        //EntityTransaction transaction = em.getTransaction();
        //transaction.begin();
        //Session session = (Session) em.getDelegate();
        //session.setFlushMode(FlushMode.MANUAL);
        //int batchSize = 100;
        //int i=0;
        //for (T bean : beans) {
        //    session.save(bean);
        //    i++;
        //    if(i%batchSize==0){
        //        session.flush();
        //        session.clear();
        //    }
        //}
        //transaction.commit();

        return beans.size();
    }

    protected Class<T> getBeanClass(){
        if(null == beanClass){
            ParameterizedType parameterizedType = (ParameterizedType)this.getClass().getGenericSuperclass();
            beanClass = (Class<T>)parameterizedType.getActualTypeArguments()[0];
        }
        return beanClass;
    }
}
