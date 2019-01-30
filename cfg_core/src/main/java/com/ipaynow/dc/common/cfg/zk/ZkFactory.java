package com.ipaynow.dc.common.cfg.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Date: 2019/1/29 11:24
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.zk
 * @ClassName: ZkFactory
 * @Description:
 **/
public class ZkFactory {
    public static final Logger logger = LoggerFactory.getLogger(ZkFactory.class);
    private static ZkFactory instance;
    private CuratorFramework client = null;

    public static ZkFactory getInstance(String zkServers, String baseDir) {
        if (null == instance) {
            synchronized (ZkFactory.class) {
                instance = new ZkFactory();
                try {
                    instance.init(zkServers, baseDir);
                } catch (Exception e) {
                    e.printStackTrace();
                    //打印日志
                }
            }
        }
        return instance;
    }

    private void init(String zkServers, String baseDir) throws InterruptedException {
        logger.info("初始化zk客户端,zkBasePath:{}", baseDir);
        client = CuratorFrameworkFactory.builder().connectString(zkServers)
                .namespace(baseDir).retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
                .connectionTimeoutMs(5000).build();
        client.start(); //启动客户端
        client.blockUntilConnected();   //链接zk
    }


    public CuratorFramework getClient() {
        return client;
    }


}
