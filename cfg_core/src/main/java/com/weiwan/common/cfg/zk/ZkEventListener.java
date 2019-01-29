package com.weiwan.common.cfg.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

/**
 * @Date: 2019/1/29 16:05
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.core.admin
 * @ClassName: ZkEventListener
 * @Description:
 **/
public class ZkEventListener implements NodeCacheListener {

    protected NodeCache nodeCache;
    protected String nodeName;

    public void nodeChanged() {
        System.out.println("监听到节点数据发生变化");
        byte[] data = nodeCache.getCurrentData().getData();
        String eventData = new String(data);
        if (null != eventData && !"".equals(eventData)) {
            ZkEvent zkEvent = new ZkEvent(eventData, nodeName);
            ZkEventDriver.handlePassiveEvent(zkEvent);
        }
    }


    public ZkEventListener(final NodeCache nodeCache) {
        this.nodeCache = nodeCache;
    }


    public ZkEventListener start(final boolean inited) throws Exception {
        this.nodeCache.getListenable().addListener(this);
        this.nodeCache.start(inited);
        return this;
    }
}
