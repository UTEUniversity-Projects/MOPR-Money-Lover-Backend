package com.mobile.api.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Map<String, String> getCookies(String clientId) {
        Map<Object, Object> raw = redisTemplate.opsForHash().entries("cookie:" + clientId);
        Map<String, String> result = new HashMap<>();
        raw.forEach((k, v) -> result.put(String.valueOf(k), String.valueOf(v)));
        return result;
    }

    public void saveCookies(String clientId, Map<String, String> cookies) {
        redisTemplate.opsForHash().putAll("cookie:" + clientId, cookies);
    }
}