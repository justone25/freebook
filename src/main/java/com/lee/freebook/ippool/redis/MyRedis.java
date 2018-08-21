package com.lee.freebook.ippool.redis;

import com.lee.freebook.ippool.ipmodel.IpMessage;
import com.lee.freebook.ippool.ipmodel.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

public class MyRedis {
    private static final Logger log = LoggerFactory.getLogger(MyRedis.class);
    Jedis  jedis = RedisDB.getJedis();
    //将ip信息保存在Redis列表中
    public void setIPToList(List<IpMessage> IpMessages) {
        for (IpMessage IpMessage : IpMessages) {
            //首先将IpMessage进行序列化
            byte[] bytes = SerializeUtil.serialize(IpMessage);

            jedis.rpush("IPPool".getBytes(), bytes);
        }
    }

    //将Redis中保存的对象进行反序列化
    public IpMessage getIPByList() {
        int rand = (int)(Math.random()*jedis.llen("IPPool"));

        Object o = SerializeUtil.unserialize(jedis.lindex("IPPool".getBytes(), 0));
        if (o instanceof IpMessage) {
            return (IpMessage)o;
        } else {
            log.info("不是IpMessage的一个实例~");
            return null;
        }
    }

    public void deleteKey(String key) {
        jedis.del(key);
    }

    public void close() {
        RedisDB.close(jedis);
    }
}
