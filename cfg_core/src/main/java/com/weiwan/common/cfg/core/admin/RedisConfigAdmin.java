package com.weiwan.common.cfg.core.admin;

import com.weiwan.common.cfg.core.ConfigCenter;
import com.weiwan.common.cfg.pojo.Config;
import com.weiwan.common.cfg.utils.RedisFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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


    public RedisConfigAdmin(Map<String, String> configuration, ConfigCenter configCenter) throws Exception {
        super(configuration, configCenter);
    }

    public boolean reloadConfig(String modelKey, boolean isFull) {
        System.out.println("要Reload的Model: " + modelKey);
        Config config = new Config();
        Jedis jedis = redisInstance.getJedisClient();
        Map<String, String> modelMap = jedis.hgetAll(modelKey);
        handleModelMap(config, jedis, modelMap);
        return super.reloadConfigToCache(modelKey, config);
    }


    public boolean loadConfig(String modelKey) {
        System.out.println("要Reload的Model: " + modelKey);
        Config config = new Config();
        Jedis jedis = redisInstance.getJedisClient();
        Map<String, String> modelMap = jedis.hgetAll(modelKey);
        handleModelMap(config, jedis, modelMap);
        boolean b = super.addConfigToCache(modelKey, config);
        return b;
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

    private void handleModelMap(Config config, Jedis jedis, Map<String, String> modelMap) {
        for (String key : modelMap.keySet()) {
            if (!key.startsWith("_")) {
                config.put(key, modelMap.get(key));
            } else if (key.startsWith("_map_")) {
                Map<String, String> _mapConfig = jedis.hgetAll(key);
                config.put(key, _mapConfig);
            } else if (key.startsWith("_list_")) {
                String[] split = modelMap.get(key).split(",");
                config.put(key, Arrays.asList(split));
            }
        }
    }

}
