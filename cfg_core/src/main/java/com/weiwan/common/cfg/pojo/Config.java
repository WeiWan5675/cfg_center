package com.weiwan.common.cfg.pojo;

import java.io.Serializable;

/**
 * @Date: 2019/1/29 12:29
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.pojo
 * @ClassName: Config
 * @Description:
 **/
public class Config extends AbstractConfig implements Cfg, Serializable {
    private String modelName;
    private String version;
    private boolean enable;


}
