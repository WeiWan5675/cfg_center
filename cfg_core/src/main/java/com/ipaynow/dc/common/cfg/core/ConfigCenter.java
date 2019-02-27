package com.ipaynow.dc.common.cfg.core;

import com.alibaba.fastjson.JSONObject;
import com.ipaynow.dc.common.cfg.core.admin.Admin;
import com.ipaynow.dc.common.cfg.core.admin.DBConfigAdmin;
import com.ipaynow.dc.common.cfg.core.admin.HttpConfigAdmin;
import com.ipaynow.dc.common.cfg.core.admin.RedisConfigAdmin;
import com.ipaynow.dc.common.cfg.pojo.Config;
import com.ipaynow.dc.common.cfg.pojo.CfgException;
import com.ipaynow.dc.common.cfg.pojo.ConfigEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Date: 2019/1/29 11:19
 * @Author: xiaozhennan
 * @Package: com.ipaynow.dc.common.cfg.core
 * @ClassName: ConfigCenter
 * @Description: 配置组件主类, 用户创建配置中心的对象/作为组件植入代码中
 **/
public class ConfigCenter {

    public static final Logger logger = LoggerFactory.getLogger(ConfigCenter.class);

    //单例对象
    private static ConfigCenter instance;
    //配置缓存
    private Map<String, Config> cache = new ConcurrentHashMap<String, Config>();
    //初始化配置信息
    private Map<String, String> configuration;

    private Admin admin;

    ReadWriteLock rwlock = new ReentrantReadWriteLock();

    /**
     * 初始化配置中心
     */
    private void init() throws Exception {
        //进行本地配置的加载
        logger.info("开始初始化ConfigCenter");
        Properties pros = new Properties();
        InputStream in = null;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream("configcenter.properties");
            logger.info("加载默认配置文件");
            pros.load(in);
        } catch (IOException e) {
            logger.error("加载默认配置文件出错", e);
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
        }
        for (String key : configuration.keySet()) {
            pros.put(key, configuration.get(key));
        }
        configuration = (Map) pros;
        String _type = configuration.get("cfg.db.type");
        logger.info("配置管理器选择:{},", _type);
        if (_type.equals(ConfigEnum.ADMIN_TYPE_DB.key())) {
            admin = new DBConfigAdmin(configuration, this);
        } else if (_type.equals(ConfigEnum.ADMIN_TYPE_HTTP.key())) {
            admin = new HttpConfigAdmin(configuration, this);
        } else if (_type.equals(ConfigEnum.ADMIN_TYPE_REDIS.key())) {
            admin = new RedisConfigAdmin(configuration, this);
        } else {
            //没有对应的
            logger.error("没有配置任何配置管理器类型!请检查配置");
            throw new CfgException("9999", "参数错误");
        }

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
                } catch (Exception e) {
                    e.printStackTrace();
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
        try {
            rwlock.readLock().lock();
            return cache;
        } finally {
            rwlock.readLock().unlock();
        }
    }

    public static Admin getAdmin() {
        return instance.admin;
    }


    public boolean put(String key, Config config) {
        rwlock.writeLock().lock();
        try {
            cache.put(key, config);
            return true;
        } finally {
            rwlock.writeLock().unlock();
        }
    }

    public Config get(String key) {
        try {
            rwlock.readLock().lock();
            return cache.get(key);
        } finally {
            rwlock.readLock().unlock();
        }
    }

    public boolean isExist(String key) {
        return cache.containsKey(key);
    }

    public boolean remove(String key) {
        rwlock.writeLock().lock();

        try {
            Config remove = cache.remove(key);
            if (remove != null) {
                return true;
            }
            return false;
        } finally {
            rwlock.writeLock().unlock();
        }
    }

    public boolean replaceAll(Map<String, Config> tmpCache) {
        try {
            rwlock.writeLock().lock();
            for (String key : tmpCache.keySet()) {
                cache.put(key, tmpCache.get(key));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            rwlock.writeLock().unlock();
        }
    }


    public String printCacheStatus() {
        JSONObject json = new JSONObject();
        json.put("config_center", cache);
        return json.toJSONString();
    }


    public Config getModel(String modelKey) {
        rwlock.readLock().lock();
        try {
            return cache.get(modelKey);
        } finally {
            rwlock.readLock().unlock();
        }
    }
}
