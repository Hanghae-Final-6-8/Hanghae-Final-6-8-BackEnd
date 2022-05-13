package com.hanghae.coffee.config.redis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

	@Value("${spring.redis-embedded.port}")
	private int redisPort;

	private RedisServer redisServer;

	@PostConstruct
	public void redisServer() {
		redisServer = RedisServer.builder()
				.port(redisPort)
				//.redisExecProvider(customRedisExec) //com.github.kstyrc (not com.orange.redis-embedded)
				.setting("maxmemory 128M") //maxheap 128M
				.build();
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		if (redisServer != null) {
			redisServer.stop();
		}
	}
}