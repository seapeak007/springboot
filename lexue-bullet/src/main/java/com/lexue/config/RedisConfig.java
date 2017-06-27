package com.lexue.config;

//import com.lexue.utils.SerializableUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * The type Redis config.
 */
@Configuration
public class RedisConfig {

    private final static StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    /**
     * Init redis template.
     *
     * @param <T>                    the type parameter
     * @param redisConnectionFactory the redis connection factory
     * @param clazz                  the clazz
     * @return the redis template
     */
//    public static <T> RedisTemplate<String, T> init(RedisConnectionFactory redisConnectionFactory, Class<T> clazz) {
//        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
//        Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(clazz);
//        jackson2JsonRedisSerializer.setObjectMapper(SerializableUtils.json);
//
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

    /**
     * Wei xin group info redis template redis template.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the redis template
     */
//    @Bean
//    public RedisTemplate<String, UserLoginInfo> userLoginInfoRedisTemplate(RedisConnectionFactory
//                                                                                   redisConnectionFactory) {
//        return init(redisConnectionFactory, UserLoginInfo.class);
//    }

    /**
     * Long redis template redis template.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the redis template
     */
//    @Bean
//    public RedisTemplate<String, Long> longRedisTemplate(RedisConnectionFactory
//                                                                 redisConnectionFactory) {
//        return init(redisConnectionFactory, Long.class);
//    }

    /**
     * String redis template string redis template.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the string redis template
     */
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory);
//        stringRedisTemplate.setDefaultSerializer(stringRedisSerializer);
//        stringRedisTemplate.setKeySerializer(stringRedisSerializer);
//        stringRedisTemplate.setHashKeySerializer(stringRedisSerializer);
//        stringRedisTemplate.setValueSerializer(stringRedisSerializer);
//        stringRedisTemplate.setHashValueSerializer(stringRedisSerializer);
//        stringRedisTemplate.afterPropertiesSet();
//        return stringRedisTemplate;
//    }

}
