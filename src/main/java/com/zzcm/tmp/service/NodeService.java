package com.zzcm.tmp.service;

import com.zzcm.tmp.bean.Node;

import java.util.List;

/**
 * Created by Administrator on 2015/12/23.
 */
public interface NodeService extends BaseService<Node,Integer>{
    public List<Node> getAllNodes();
}
