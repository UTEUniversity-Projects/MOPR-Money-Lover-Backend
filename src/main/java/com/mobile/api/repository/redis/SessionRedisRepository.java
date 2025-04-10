package com.mobile.api.repository.redis;

import com.mobile.api.model.redis.SessionRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRedisRepository extends CrudRepository<SessionRedis, String> {
}
