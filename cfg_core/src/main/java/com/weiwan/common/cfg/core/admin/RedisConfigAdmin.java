package com.weiwan.common.cfg.core.admin;

import com.weiwan.common.cfg.core.ConfigCenter;
import com.weiwan.common.cfg.pojo.Config;
import com.weiwan.common.cfg.utils.RedisFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2019/1/29 13:24
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.core
 * @ClassName: RedisConfigAdmin
 * @Description:
 **/
public class RedisConfigAdmin extends ConfigAdmin implements Admin {

    private RedisFactory redisInstance;

    public void init() throws IOException {
        Map redisConfig = new HashMap<String, String>();
        redisConfig.put("redis.ip", configuration.get("cfg.db.redis.server"));
        redisConfig.put("redis.port", configuration.get("cfg.db.redis.port"));
        redisConfig.put("redis.password", configuration.get("cfg.db.redis.pass"));
        redisInstance = RedisFactory.getInstance(redisConfig);
    }


    public boolean reloadConfig(String modelKey, String zkPath) {
        Config config = new Config();

        //根据ModelName获取配置信息
        System.out.println("reload");
        //调用ConfigAdmin的组装方法组装配置对象
        //更新配置
        //返回更新结果
        System.out.println("要Reload的Model: " + modelKey);
        System.out.println("要Reload的Path: " + zkPath);
        return false;
    }


    public RedisConfigAdmin(Map<String, String> configuration, ConfigCenter configCenter) throws Exception {
        super(configuration, configCenter);
    }

    public boolean reloadConfig(String modelKey, boolean isFull) {
        return false;
    }

    public boolean loadConfig(String modelKey, boolean isFull) {
        return false;
    }

    public boolean deleteConfig(String modelKey) {
        return false;
    }

    public boolean disableConfig(String modelKey) {
        return false;
    }

    public boolean enableConfig(String modelKey) {
        return false;
    }

    public boolean clearConfig() {
        return false;
    }
}
