package com.ipaynow.dc.common.cfg.core.admin;

import com.ipaynow.dc.common.cfg.core.ConfigCenter;

import java.util.List;
import java.util.Map;

/**
 * @Date: 2019/1/29 13:25
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.core
 * @ClassName: DbConfigAdmin
 * @Description:
 **/
public class DBConfigAdmin extends ConfigAdmin implements Admin {


    public DBConfigAdmin(Map<String, String> configuration, ConfigCenter configCenter) throws Exception {
        super(configuration, configCenter);
    }

    public void init() {

    }


    public boolean reloadConfig(String modelKey, boolean isFull) {
        return false;
    }

    public boolean loadConfig(String modelKey) {
        return false;
    }

    public boolean deleteConfig(String modelKey, boolean isFull) {
        return false;
    }

    public boolean disableConfig(String modelKey, boolean isFull) {
        return false;
    }

    public boolean enableConfig(String modelKey, boolean isFull) {
        return false;
    }


    public boolean loadConfigs(List<String> models) {
        return false;
    }
}
