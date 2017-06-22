package com.lexue.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by UI03 on 2017/6/22.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String META_CACHE = "META_CACHE";
    public static final String LIVE_ROOM = "LIVE_ROOM_CACHE" ;

    @Bean
    public Cache domainCache() {
        return new GuavaCache(META_CACHE, CacheBuilder.newBuilder()
                .maximumSize(3)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build());
    }

    @Bean
    public Cache liveRoomCache(){
        return new GuavaCache(LIVE_ROOM,CacheBuilder.newBuilder()
                .maximumSize(50)
                .expireAfterWrite(600,TimeUnit.SECONDS)
                .build()) ;
    }

}