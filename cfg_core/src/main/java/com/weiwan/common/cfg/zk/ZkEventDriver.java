package com.weiwan.common.cfg.zk;

import com.weiwan.common.cfg.core.ConfigCenter;
import com.weiwan.common.cfg.core.admin.Admin;

/**
 * @Date: 2018/10/17 10:34
 * @Author: xiaozhennan
 * @Package: com.ipaynow.dc.provider.distributed
 * @ClassName: ZkEventDriver
 * @Description: 处理zk节点变化事件
 **/
public class ZkEventDriver {

    private static Admin admin = ConfigCenter.getAdmin();

    /**
     * 处理被动接收到的节点数据变化事件
     *
     * @param zkEvent
     * @return
     */
    public static boolean handlePassiveEvent(ZkEvent zkEvent) {
        String eventData = zkEvent.getEventData();
        String nodeName = zkEvent.getNodeName();
        return admin.reloadConfig(eventData, nodeName);
    }


}
