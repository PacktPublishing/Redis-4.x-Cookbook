package packt.rediscookbook.springdataredisdemo.config;

import packt.rediscookbook.springdataredisdemo.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class AppConfig {
    private @Value("${redis.host}") String redisHost;
    private @Value("${redis.port}") int redisPort;

    @Bean
    JedisConnectionFactory jedisConnectionFactory(){
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(redisHost);
		factory.setPort(redisPort);
		factory.setUsePool(true);
		return factory;
	}

	@Bean
	RedisTemplate<String, User> redisTemplate(){
		RedisTemplate<String, User> template = new RedisTemplate<>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}

}
