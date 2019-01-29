package com.weiwan.common.cfg.core.admin;

import com.weiwan.common.cfg.core.ConfigCenter;
import com.weiwan.common.cfg.pojo.Config;
import com.weiwan.common.cfg.zk.ZkEventListener;
import com.weiwan.common.cfg.zk.ZkFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;


import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2019/1/29 11:25
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.core
 * @ClassName: ConfigManager
 * @Description: 配置中心管理者
 **/
public abstract class ConfigAdmin {

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
        System.out.println("开始加载配置");
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
        System.out.println("初始化Zk节点");
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
        System.out.println("初始化zk监听");
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
        }
        return false;
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
