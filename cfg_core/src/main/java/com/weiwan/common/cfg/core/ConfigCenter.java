package com.weiwan.common.cfg.core;

import com.weiwan.common.cfg.pojo.Config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2019/1/29 11:19
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.core
 * @ClassName: ConfigCenter
 * @Description: 配置组件主类, 用户创建配置中心的对象/作为组件植入代码中
 **/
public class ConfigCenter {

    //单例对象
    private static ConfigCenter instance;
    //配置缓存
    private Map<String, Config> cache = new ConcurrentHashMap<String, Config>();
    //初始化配置信息
    private Map<String, String> configuration;

    private ConfigAdmin admin;

    /**
     * 初始化配置中心
     */
    private void init() throws IOException {
        this.admin = ConfigAdmin.getInstance(this.configuration, this);
    }


    /**
     * 单例方法
     *
     * @param configuration
     * @return
     */
    public static ConfigCenter createInstance(Map<String, String> configuration) {
        if (null == instance) {
            synchronized (ConfigCenter.class) {
                instance = new ConfigCenter(configuration, new ConcurrentHashMap<String, Config>());
                try {
                    instance.init();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return instance;
    }

    private ConfigCenter() {
    }

    private ConfigCenter(Map<String, String> configuration, ConcurrentHashMap<String, Config> cache) {
        this.configuration = configuration;
        this.cache = cache;
    }

    public Map<String, Config> getCache() {
        return cache;
    }

    public ConfigAdmin getAdmin() {
        return admin;
    }

}
