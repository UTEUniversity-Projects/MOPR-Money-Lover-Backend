package com.mobile.api.model.redis;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("system_session_redis")
public class SessionRedis {
    @Id
    private String id;
    private String userId;
    private String username;
    private Long createdAt;
    private Long expiredAt;
}
