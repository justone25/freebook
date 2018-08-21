package com.lee.freebook.ippool.redis;

import redis.clients.jedis.Jedis;

import java.util.ResourceBundle;

public class RedisDB {
    private static String addr;
    private static int port;
    private static String passwd;

    //加载配置文件
    private static ResourceBundle rb = ResourceBundle.getBundle("redis-config");

    static {
        addr = rb.getString("jedis.addr");
        port = Integer.parseInt(rb.getString("jedis.port"));
        passwd = rb.getString("jedis.passwd");
    }

    //获取Jedis实例
    public synchronized static Jedis getJedis() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis(addr, port);
        //权限认证
//        jedis.auth(passwd);

        return jedis;
    }

    //释放Jedis资源
    public static void close(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
