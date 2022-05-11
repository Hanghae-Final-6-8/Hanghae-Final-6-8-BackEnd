package com.hanghae.coffee.utils;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisUtils {

    private final StringRedisTemplate stringRedisTemplate;

    public String getRedisData(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
        stringRedisTemplate.expire(key, duration, TimeUnit.MILLISECONDS);
    }

    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

}
