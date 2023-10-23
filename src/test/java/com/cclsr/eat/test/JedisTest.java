package com.cclsr.eat.test;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

public class JedisTest {
    @Test
    public void testRedis(){
        // 1 获取连接
        Jedis jedis = new Jedis("localhost", 6379);
        // 2 执行具体的操作
        jedis.set("username","xiaoming");

        String value = jedis.get("username");
        System.out.println(value);
        // 3 关闭连接
        jedis.close();
    }
}
