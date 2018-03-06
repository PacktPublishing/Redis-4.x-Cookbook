package com.packtpub.redis_cookbook;

import redis.clients.jedis.Jedis;
import java.util.List;

public class JedisSingleDemo {
    public static void main(String[] args) {
        //Connecting to localhost Redis server
        Jedis jedis = new Jedis("localhost");

        //String operations
        String restaurant = "Extreme Pizza";
        jedis.set(restaurant, "300 Broadway, New York, NY");
        jedis.append(restaurant, " 10011");
        String address = jedis.get("Extreme Pizza");
        System.out.printf("Address for %s is %s\n", restaurant, address);

        //List operations
        String listKey = "favorite_restaurants";
        jedis.lpush(listKey, "PF Chang's", "Olive Garden");
        jedis.rpush(listKey, "Outback Steakhouse", "Red Lobster");
        List<String> favoriteRestaurants = jedis.lrange(listKey, 0, -1);
        System.out.printf("Favorite Restaurants: %s\n", favoriteRestaurants);

        System.exit(0);
    }
}
