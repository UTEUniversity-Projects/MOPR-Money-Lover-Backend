package com.mobile.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.mobile.api.repository.jpa")
@EnableRedisRepositories(basePackages = "com.mobile.api.repository.redis")
public class RepositoryConfig {
}

