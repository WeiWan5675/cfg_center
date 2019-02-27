package com.ipaynow.dc.common.cfg.core.admin;

import com.ipaynow.dc.common.cfg.core.ConfigCenter;
import com.ipaynow.dc.common.cfg.pojo.Config;
import com.ipaynow.dc.common.cfg.zk.ZkEventListener;
import com.ipaynow.dc.common.cfg.zk.ZkFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

/**
 * @Date: 2019/1/29 11:25
 * @Author: xiaozhennan
 * @Package: com.ipaynow.dc.common.cfg.core
 * @ClassName: ConfigManager
 * @Description: 配置中心管理者
 **/
public abstract class ConfigAdmin {
    public static final Logger logger = LoggerFactory.getLogger(ConfigAdmin.class);
    protected final ConfigCenter context;
    protected Map<String, String> configuration;
    protected ZkFactory zkFactory;
    protected List<ZkEventListener> listeners;

    public ConfigAdmin(Map<String, String> configuration, ConfigCenter configCenter) throws Exception {
        this.configuration = configuration;
        this.context = configCenter;
        this.initSuper();
    }


    public void initSuper() throws Exception {
        //初始化配置
        logger.info("开始加载配置");
        //初始化应用节点
        String baseDir = configuration.get("cfg.zk.base.dir");
        String zkServers = configuration.get("cfg.zk.servers");
        this.zkFactory = ZkFactory.getInstance(zkServers, baseDir);
        this.listeners = new ArrayList<ZkEventListener>();
        this.init();
        _initZkNode();
        _initZkWatch();

    }

    public abstract void init() throws IOException;

    private void _initZkNode() throws Exception {
        CuratorFramework client = zkFactory.getClient();
        logger.info("初始化Zk节点");
//创建基础目录
        String confDir = configuration.get("cfg.zk.conf.dir");
        String lockDir = configuration.get("cfg.zk.lock.dir");
        String clusterDir = configuration.get("cfg.zk.cluster.dir");
        Stat stat_cf = client.checkExists().forPath(confDir);
        Stat stat_lk = client.checkExists().forPath(lockDir);
        Stat stat_cl = client.checkExists().forPath(clusterDir);
        if (stat_cf == null) {
            client.create().forPath(confDir);
        }
        if (stat_lk == null) {
            client.create().forPath(lockDir);
        }
        if (stat_cl == null) {
            client.create().forPath(clusterDir);
        }
        String ip = InetAddress.getLocalHost().getHostAddress().toString();

        Stat clusterDirNode = client.checkExists().forPath(clusterDir + "/" + ip);
        if (clusterDirNode != null) {
            client.delete().forPath(clusterDir + "/" + ip);
        }
        Stat confDirNode = client.checkExists().forPath(confDir + "/" + ip);
        if (confDirNode != null) {
            client.delete().forPath(confDir + "/" + ip);
        }
        //注册自身临时服务节点
        client.create().withMode(CreateMode.EPHEMERAL).forPath(clusterDir + "/" + ip, "runing".getBytes());
        //注册自身配置状态节点
        client.create().withMode(CreateMode.EPHEMERAL).forPath(confDir + "/" + ip, (new Date().getTime() + "").getBytes());
    }

    protected void _initZkWatch() throws Exception {
        CuratorFramework client = zkFactory.getClient();
        logger.info("初始化zk监听");
        NodeCache cache = new NodeCache(client, configuration.get("cfg.zk.config.watch.dir"), false);
        ZkEventListener listener = new ZkEventListener(cache);
        listener.start(false);
        listeners.add(listener);
    }


    public boolean addConfigToCache(String modelKey, Config config) {
        return context.put(modelKey, config);
    }

    public boolean reloadConfigToCache(String modelKey, Config config) {
        Config oldConfig = context.get(modelKey);
        if (oldConfig != null) {
            boolean remove = context.remove(modelKey);
            if (remove) {
                boolean isOk = context.put(modelKey, config);
                return isOk;
            }
        } else {
            return context.put(modelKey, config);
        }
        return false;
    }

    public Set<String> catConfigList() {
        //TODO 优化点
        return context.getCache().keySet();
    }

    public boolean reloadAllConfig(Map<String, Config> tmpCache) {
        return context.replaceAll(tmpCache);
    }

    public String catConfigStatus() {
        return context.printCacheStatus();
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
