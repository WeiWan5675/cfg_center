package com.weiwan.common.cfg.core;

import com.weiwan.common.cfg.pojo.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2019/1/29 11:25
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.core
 * @ClassName: ConfigManager
 * @Description: 配置中心管理者
 **/
public class ConfigAdmin {

    private static ConfigAdmin instance;
    private final ConfigCenter context;
    private Map<String, String> systemConf;

    public ConfigAdmin(Map<String, String> configuration, ConfigCenter configCenter) {
        this.systemConf = configuration;
        this.context = configCenter;
    }

    public static ConfigAdmin getInstance(Map<String, String> configuration, ConfigCenter configCenter) throws IOException {
        if (null == instance) {
            synchronized (ConfigCenter.class) {
                instance = new ConfigAdmin(configuration, configCenter);
                instance.init();
            }
        }
        return instance;
    }


    private void init() throws IOException {
        //进行本地配置的加载
        Properties pros = new Properties();
        InputStream in = null;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream("configcenter.properties");
            pros.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }



    }


    /**
     * 配置管理
     * 1. 所有配置管理都以zk事件进行驱动
     * 2. 配置管理者负责组装配置对象
     * 3. 负责链接redis控制zk状态等
     * 4. 负责配置的更新失效添加等
     * 5. 本地锁
     */

}
