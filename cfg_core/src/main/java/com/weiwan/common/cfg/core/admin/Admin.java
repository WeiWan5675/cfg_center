package com.weiwan.common.cfg.core.admin;

/**
 * @Date: 2019/1/29 13:26
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.core
 * @ClassName: Admin
 * @Description:
 **/
public interface Admin {


    boolean reloadConfig(String modelKey, String zkPath);
}
