package com.ipaynow.dc.common.cfg.zk;

import com.ipaynow.dc.common.cfg.core.ConfigCenter;
import com.ipaynow.dc.common.cfg.core.admin.Admin;
import com.ipaynow.dc.common.cfg.pojo.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ipaynow.dc.common.cfg.pojo.EventType.LOAD;

/**
 * @Date: 2018/10/17 10:34
 * @Author: xiaozhennan
 * @Package: com.ipaynow.dc.provider.distributed
 * @ClassName: ZkEventDriver
 * @Description: 处理zk节点变化事件
 **/
public class ZkEventDriver {

    private static Admin admin = ConfigCenter.getAdmin();

    static Logger logger = LoggerFactory.getLogger(ZkEventDriver.class);

    /**
     * 处理被动接收到的节点数据变化事件
     *
     * @param zkEvent
     * @return
     */
    public static boolean handleEvent(ZkEvent zkEvent) {
//
//
//        switch (zkEvent.eventType) {
//            case LOAD:
//                //获取所有的
//                break;
//            case LOAD_ALL:
//                break;
//            case DELETE:
//                break;
//            case DELETE_ALL:
//                break;
//            case ENABLE:
//                break;
//            case ENABLE_ALL:
//                break;
//            case DISABLE:
//                break;
//            case DISABLE_ALL:
//                break;
//        }

        String eventData = zkEvent.getEventData();
        if (eventData != null && eventData.equals("reload_all")) {
            logger.info("reload_all事件触发");
            return admin.reloadConfig(eventData, true);
        } else {
            return admin.reloadConfig(eventData, false);
        }
    }


}
