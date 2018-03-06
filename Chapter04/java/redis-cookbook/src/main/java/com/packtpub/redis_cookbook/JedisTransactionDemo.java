package com.packtpub.redis_cookbook;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import java.util.Set;

public class JedisTransactionDemo {
    public static void main(String[] args) {
        //Connecting to localhost Redis server
        Jedis jedis = new Jedis("localhost");

        //Initialize
        String user = "user:1000";
        String restaurantOrderCount = "restaurant_orders:200";
        String restaurantUsers = "restaurant_users:200";
        jedis.set(restaurantOrderCount, "400");
        jedis.sadd(restaurantUsers, "user:302", "user:401");

        //Create a Redis transaction
        Transaction transaction = jedis.multi();
        Response<Long> countResponse = transaction.incr(restaurantOrderCount);
        transaction.sadd(restaurantUsers, user);
        Response<Set<String>> userSet = transaction.smembers(restaurantUsers);
        //Execute transaction
        transaction.exec();

        //Handle responses
        System.out.printf("Number of orders: %d\n", countResponse.get());
        System.out.printf("Users: %s\n", userSet.get());
        System.exit(0);
    }
}


