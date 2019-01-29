package com.weiwan.common.cfg.core.admin;

import com.weiwan.common.cfg.core.ConfigCenter;

import java.util.Map;

/**
 * @Date: 2019/1/29 13:25
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.core
 * @ClassName: HttpConfigAdmin
 * @Description:
 **/
public class HttpConfigAdmin extends ConfigAdmin implements Admin {


    public HttpConfigAdmin(Map<String, String> configuration, ConfigCenter configCenter) throws Exception {
        super(configuration, configCenter);
    }

    public void init() {

    }

    public boolean reloadConfig(String modelKey, String zkPath) {
        return false;
    }
}
