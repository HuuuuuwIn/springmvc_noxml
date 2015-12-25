package com.zzcm.tmp.service.impl;

import com.zzcm.tmp.bean.Node;
import com.zzcm.tmp.dao.BaseDao;
import com.zzcm.tmp.dao.NodeDao;
import com.zzcm.tmp.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2015/12/23.
 */
@Component
public class NodeServiceImpl extends BaseServiceImpl<Node,Integer> implements NodeService {
    private static final Logger LOG = LoggerFactory.getLogger(NodeServiceImpl.class);

    @Autowired
    private NodeDao dao;


    @Override
    public List<Node> getAllNodes() {
        return dao.getAll();
    }

    @Override
    protected BaseDao<Node, Integer> getBaseDao() {
        return dao;
    }
}
