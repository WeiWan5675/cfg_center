package com.ipaynow.dc.common.cfg.core.admin;

import com.ipaynow.dc.common.cfg.pojo.Config;
import com.ipaynow.dc.common.cfg.utils.RedisFactory;
import com.ipaynow.dc.common.cfg.core.ConfigCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2019/1/29 13:24
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.core
 * @ClassName: RedisConfigAdmin
 * @Description:
 **/
public class RedisConfigAdmin extends ConfigAdmin implements Admin {

    public static final Logger logger = LoggerFactory.getLogger(RedisConfigAdmin.class);

    private RedisFactory redisInstance;

    public void init() throws IOException {
        logger.info("开始初始化配置管理器");
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
        Jedis jedis = redisInstance.getJedisClient();
        try {
            if (isFull) {
                logger.info("开始reload所有数据");
                Map<String, Config> tmpCache = new ConcurrentHashMap<String, Config>();
                Set<String> configs = catConfigList();
                for (String configKey : configs) {
                    Config config = new Config(configKey);
                    Map<String, String> modelMap = jedis.hgetAll(configKey);
                    handleModelMap(config, jedis, modelMap);
                    tmpCache.put(configKey, config);
                }
                boolean flag = reloadAllConfig(tmpCache);
                logger.info( catConfigStatus());
                return flag;
            } else {
                logger.info("要Reload的Model:{}", modelKey);
                Config config = new Config(modelKey);
                Map<String, String> modelMap = jedis.hgetAll(modelKey);
                handleModelMap(config, jedis, modelMap);
                boolean flag = reloadConfigToCache(modelKey, config);
                logger.info( catConfigStatus());
                return flag;
            }
        } finally {
            redisInstance.closeClient(jedis);
        }
    }


    public boolean loadConfig(String modelKey) {
        logger.info("要load的Model:{}", modelKey);
        Config config = new Config();
        Jedis jedis = redisInstance.getJedisClient();
        try {
            Map<String, String> modelMap = jedis.hgetAll(modelKey);
            handleModelMap(config, jedis, modelMap);
            boolean flag = addConfigToCache(modelKey, config);
            logger.info( catConfigStatus());
            return flag;
        } finally {
            redisInstance.closeClient(jedis);
        }
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
