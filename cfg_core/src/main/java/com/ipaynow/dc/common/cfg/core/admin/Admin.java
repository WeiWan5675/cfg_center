package com.ipaynow.dc.common.cfg.core.admin;

import java.util.List;

/**
 * @Date: 2019/1/29 13:26
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.core
 * @ClassName: Admin
 * @Description:
 **/
public interface Admin {


    boolean reloadConfig(String modelKey, boolean isFull);

    boolean loadConfig(String modelKey);

    boolean deleteConfig(String modelKey, boolean isFull);

    boolean disableConfig(String modelKey, boolean isFull);

    boolean enableConfig(String modelKey, boolean isFull);

    boolean loadConfigs(List<String> models);
}
