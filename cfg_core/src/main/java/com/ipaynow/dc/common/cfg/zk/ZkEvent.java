package com.ipaynow.dc.common.cfg.zk;

import com.ipaynow.dc.common.cfg.pojo.EventType;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

/**
 * @Date: 2018/10/17 10:36
 * @Author: xiaozhennan
 * @Package: com.ipaynow.dc.provider.distributed
 * @ClassName: ZkEvent
 * @Description:zk节点事件
 **/
public class ZkEvent {

    protected String nodeName;
    protected String eventData;
    protected EventType eventType;

    public ZkEvent(String eventData, String nodeName) {
        this.eventData = eventData;
        this.nodeName = nodeName;
    }

    public ZkEvent(String eventData, EventType type) {
        this.eventData = eventData;
        this.eventType = type;
    }

    public ZkEvent(EventType eventType) {
    }

    public ZkEvent(String eventData) {
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getEventData() {
        return eventData;
    }

}
