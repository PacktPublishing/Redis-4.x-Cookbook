package com.packtpub.redis_cookbook;

import redis.clients.jedis.Jedis;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JedisLuaDemo {
    public static void main(String[] args) throws Exception {
        //Connecting to localhost Redis server
        Jedis jedis = new Jedis("localhost");

        String user = "users:id:992452";
        jedis.set(user, "{\"name\": \"Tina\", \"sex\": \"female\", \"grade\": \"A\"}");

        //Register Lua script
        InputStream luaInputStream =
            JedisLuaDemo.class
                .getClassLoader()
                .getResourceAsStream("updateJson.lua");
        String luaScript =
            new BufferedReader(new InputStreamReader(luaInputStream))
            .lines()
            .collect(Collectors.joining("\n"));
        String luaSHA = jedis.scriptLoad(luaScript);

        //Eval Lua script
        List<String> KEYS = Collections.singletonList(user);
        List<String> ARGS = Collections.singletonList("{\"grade\": \"C\"}");
        jedis.evalsha(luaSHA, KEYS, ARGS);

        System.out.printf("%s: %s\n", user, jedis.get(user));
        System.exit(0);
    }
}


