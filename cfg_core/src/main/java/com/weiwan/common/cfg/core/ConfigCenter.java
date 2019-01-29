package com.weiwan.common.cfg.core;

import com.weiwan.common.cfg.core.admin.Admin;
import com.weiwan.common.cfg.core.admin.DBConfigAdmin;
import com.weiwan.common.cfg.core.admin.HttpConfigAdmin;
import com.weiwan.common.cfg.core.admin.RedisConfigAdmin;
import com.weiwan.common.cfg.pojo.Config;
import com.weiwan.common.cfg.pojo.ConfigEnum;

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

    private Admin admin;

    ReadWriteLock rwlock = new ReentrantReadWriteLock();

    /**
     * 初始化配置中心
     */
    private void init() throws Exception {
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
        //TODO 将本地配置和用户配置混合到一起
        for (String key : configuration.keySet()) {
            pros.put(key, configuration.get(key));
        }
        configuration = (Map) pros;
        String _type = configuration.get("cfg.db.type");
        if (_type.equals(ConfigEnum.ADMIN_TYPE_DB.key())) {
            admin = new DBConfigAdmin(configuration, this);
        } else if (_type.equals(ConfigEnum.ADMIN_TYPE_HTTP.key())) {
            admin = new HttpConfigAdmin(configuration, this);
        } else if (_type.equals(ConfigEnum.ADMIN_TYPE_REDIS.key())) {
            admin = new RedisConfigAdmin(configuration, this);
        } else {
            //没有对应的
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
        return cache;
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
}
