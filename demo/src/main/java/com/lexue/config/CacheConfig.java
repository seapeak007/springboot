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

    public static final String META_CACHE = "META_CACHE";

    public static final String INDUSER_CACHE = "INDUSER_CACHE";

    @Bean
    public Cache domainCache() {
        return new GuavaCache(META_CACHE, CacheBuilder.newBuilder()
                .maximumSize(3)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build());
    }

    @Bean
    public Cache secretCache() {
        return new GuavaCache(INDUSER_CACHE, CacheBuilder.newBuilder()
                .maximumSize(100000)
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build());
    }

}