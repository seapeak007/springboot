package com.lexue.service.impl;

//import com.lexue.member.cache.CacheHeader;
//import com.lexue.member.config.CacheConfig;
//import com.lexue.member.service.LoginEffectiveTimeService;
//import com.lexue.type.BusinessType;
//import com.lexue.type.LoginFrom;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;

/**
 * Created by danielmiao on 2017/5/3.
 * Version: 1.0.0
 */
//@Service
//public class LoginEffectiveTimeServiceImpl implements LoginEffectiveTimeService {
//    private final static Logger logger = LoggerFactory.getLogger(LoginEffectiveTimeServiceImpl.class);
//    private static final int KEEP_TIME = 60 * 60 * 1000;
//
//    private final StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    public LoginEffectiveTimeServiceImpl(StringRedisTemplate stringRedisTemplate) {
//        this.stringRedisTemplate = stringRedisTemplate;
//    }
//
//    @Override
//    @Cacheable(CacheConfig.DOMAIN_CACHE)
//    public long getEffectiveTime(BusinessType businessType, LoginFrom loginFrom) {
//        long time = KEEP_TIME;
//        String value = this.stringRedisTemplate.opsForValue().get(String.format(CacheHeader.USER_EFFECTIVE_TIME,
//                businessType
//                        .getValue(), loginFrom.getGroup()));
//        if (value != null) {
//            try {
//                time = Long.parseLong(value);
//            } catch (NumberFormatException e) {
//                logger.error("parser time error.", e);
//            }
//        }
//        return time;
//    }
//}
