package com.weiwan.common.cfg.pojo;

/**
 * @Date: 2019/1/29 13:39
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.pojo
 * @ClassName: ConfigEnum
 * @Description:
 **/
public enum ConfigEnum {
    ADMIN_TYPE_DB("db", "admin类型DB"),
    ADMIN_TYPE_REDIS("redis", "admin类型redis"),
    ADMIN_TYPE_HTTP("http", "admin类型http");
    private Object key;
    private Object msg;

    ConfigEnum(String key, String msg) {
        this.key = key;
        this.msg = msg;
    }


    public Object key() {
        return key;
    }


    public Object msg() {
        return msg;
    }

}
