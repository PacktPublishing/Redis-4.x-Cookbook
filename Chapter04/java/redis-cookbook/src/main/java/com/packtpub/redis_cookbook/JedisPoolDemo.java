package com.packtpub.redis_cookbook;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

public class JedisPoolDemo {
    public static void main(String[] args) {
        //Creating a JedisPool of Jedis connections to localhost Redis server
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");

        //Get a Jedis connection from pool
        try (Jedis jedis = jedisPool.getResource()) {
            String restaurantName = "Kyoto Ramen";
            Map<String, String> restaurantInfo = new HashMap<>();
            restaurantInfo.put("address", "801 Mission St, San Jose, CA");
            restaurantInfo.put("phone", "555-123-6543");
            jedis.hmset(restaurantName, restaurantInfo);
            jedis.hset(restaurantName, "rating", "5.0");
            String rating = jedis.hget(restaurantName, "rating");
            System.out.printf("%s rating: %s\n", restaurantName, rating);
            //Print out hash
            for (Map.Entry<String, String> entry: jedis.hgetAll(restaurantName).entrySet()) {
                System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
            }
        }
        System.exit(0);
    }
}

