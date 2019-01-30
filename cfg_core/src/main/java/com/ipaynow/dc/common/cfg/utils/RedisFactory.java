package com.ipaynow.dc.common.cfg.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Map;

/**
 * @Date: 2018/8/23 10:59
 * @Author: xiaozhennan
 * @Package: com.ipaynow.dc.srv.factory
 * @ClassName: RedisFactory
 * @Description: 连接Redis
 **/
public class RedisFactory {


    private static RedisFactory instance = null;
    private static JedisPool pool = null;

    private RedisFactory() {
    }

    public static RedisFactory getInstance(Map<String, String> redisConfig) throws IOException {
        if (null == instance) {
            synchronized (RedisFactory.class) {
                instance = new RedisFactory();
                instance.init(redisConfig);
            }
        }
        return instance;
    }

    private void init(Map<String, String> redisConfig) throws IOException {
        JedisPoolConfig config = new JedisPoolConfig();
        //是否启用后进先出, 默认true
        config.setLifo(true);
        //最大连接数
        config.setMaxTotal(10);
        //最大空闲连接数
        config.setMaxIdle(10);
        //最小空闲连接数
        config.setMinIdle(0);
        //超时时间，-1永不超时
        config.setMaxWaitMillis(3000);
        //在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(false);
        pool = new JedisPool(config, redisConfig.get("redis.ip"), Integer.parseInt(redisConfig.get("redis.port")), 3000, redisConfig.get("redis.password"));
    }


    /**
     * 获取一个redis客户端<br/>
     *
     * @return
     */
    public Jedis getJedisClient() {
        return pool.getResource();
    }

    public void closeClient(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


}
