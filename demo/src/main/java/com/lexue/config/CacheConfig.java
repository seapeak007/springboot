package com.lexue.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by danielmiao on 2017/5/3.
 * Version: 1.0.0
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String DOMAIN_CACHE = "META_CACHE";

    public static final String SECRET_CACHE = "INDEX_USER_CACHE";

    @Bean
    public Cache domainCache() {
        return new GuavaCache(DOMAIN_CACHE, CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build());
    }

    @Bean
    public Cache secretCache() {
        return new GuavaCache(SECRET_CACHE, CacheBuilder.newBuilder()
                .maximumSize(100000)
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build());
    }

}