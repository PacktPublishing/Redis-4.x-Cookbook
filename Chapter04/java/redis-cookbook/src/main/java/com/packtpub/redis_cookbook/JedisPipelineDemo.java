package com.packtpub.redis_cookbook;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class JedisPipelineDemo {
    public static void main(String[] args) {
        //Connecting to localhost Redis server
        Jedis jedis = new Jedis("localhost");

        //Create a Pipeline
        Pipeline pipeline = jedis.pipelined();
        //Add commands to pipeline
        pipeline.set("mykey", "myvalue");
        pipeline.sadd("myset", "value1", "value2");
        Response<String> stringValue = pipeline.get("mykey");
        Response<Long> noElementsInSet = pipeline.scard("myset");
        //Send commands
        pipeline.sync();
        //Handle responses
        System.out.printf("mykey: %s\n", stringValue.get());
        System.out.printf("Number of Elements in set: %d\n", noElementsInSet.get());
        System.exit(0);
    }
}
