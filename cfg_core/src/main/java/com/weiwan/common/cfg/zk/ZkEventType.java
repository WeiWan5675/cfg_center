package com.weiwan.common.cfg.zk;

/**
 * @Date: 2018/10/18 10:44
 * @Author: xiaozhennan
 * @Package: com.ipaynow.dc.provider.distributed
 * @ClassName: ZkEventType
 * @Description:
 **/
public enum ZkEventType {
    PASSIVE, //主动事件
    ACTIVE;  //被动事件

    ZkEventType() {
    }

}
