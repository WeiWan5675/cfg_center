package com.ipaynow.dc.common.cfg.pojo;

/**
 * @Date: 2019/1/30 17:34
 * @Author: xiaozhennan
 * @Package: com.ipaynow.dc.common.cfg.pojo
 * @ClassName: EventType
 * @Description:
 **/
public enum EventType {
    LOAD("LOAD"),
    LOAD_ALL("LOAD_ALL"),
    DELETE("DELETE"),
    DELETE_ALL("DELETE_ALL"),
    ENABLE("ENABLE"),
    ENABLE_ALL("ENABLE_ALL"),
    DISABLE("DISABLE"),
    DISABLE_ALL("DISABLE_ALL");

    String type;

    EventType(String type) {
        this.type = type;
    }
}
