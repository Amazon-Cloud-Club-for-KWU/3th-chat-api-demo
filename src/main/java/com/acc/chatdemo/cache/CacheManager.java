package com.acc.chatdemo.cache;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CacheManager {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T set(String key, T value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue);
            return value;
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize value for key: " + key, e);
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        String cachedValue = redisTemplate.opsForValue().get(key);
        if (cachedValue == null) {
            return null;
        }
        try {
            return objectMapper.readValue(cachedValue, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize cached value for key: " + key, e);
        }
    }

    public <T> T get(String key, Class<T> clazz, Supplier<T> fallbackSupplier) {
        String cachedValue = redisTemplate.opsForValue().get(key);
        if (cachedValue == null) {
            T result = fallbackSupplier.get();
            if (result != null) {
                return set(key, result);
            }
        }
        try {
            return objectMapper.readValue(cachedValue, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize cached value for key: " + key, e);
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void increment(String key, long delta) {
        redisTemplate.opsForValue().increment(key);
    }
}
