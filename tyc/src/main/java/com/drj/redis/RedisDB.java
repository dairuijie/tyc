package com.drj.redis;


import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @ClassName:  RedisDB   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: drj 
 * @date:   2018年10月26日 下午5:59:15   
 *     
 * @Copyright: 2018 
 *
 */
public class RedisDB {
    private static String addr;
    private static int port;

    //加载配置文件
    private static ResourceBundle rb = ResourceBundle.getBundle("db-config");

    //初始化连接
    static {
        addr = rb.getString("jedis.addr");
        port = Integer.parseInt(rb.getString("jedis.port"));
    }

    //获取Jedis实例
    public synchronized static Jedis getJedis() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis(addr, port);

        return jedis;
    }

    //释放Jedis资源
    public static void close(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}