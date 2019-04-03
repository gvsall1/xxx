package com.exc.config.redis;

import com.exc.config.DefaultKryoContext;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    public static String BALANCE_SYNC_KEY = "ubsy-";
    public static String BALANCE_SYNC_LOCK_KEY = "l-ubsy-";
    public static String ORDERS_SYNC_LOCK_KEY = "l-osc-";
    private final Logger log = LoggerFactory.getLogger(RedisConfiguration.class);


    public RedisConnectionFactory redisConnectionFactory() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(Runtime.getRuntime().availableProcessors() * 2);
        genericObjectPoolConfig.setMaxIdle(8);
        genericObjectPoolConfig.setMinIdle(4);
        LettuceClientConfiguration cfg = LettucePoolingClientConfiguration
            .builder()
            .poolConfig(genericObjectPoolConfig)
            .build();
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
        return new LettuceConnectionFactory(redisConf, cfg);
    }


    @Bean
    public RedisTemplate getObjectRedisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate();

        redisTemplate.setDefaultSerializer(new RedisSer(DefaultKryoContext.newKryoContextFactory(kryo -> {
            kryo.register(String.class);
            kryo.register(Long.class);
            kryo.register(Boolean.class);
        })));
        redisTemplate.setEnableDefaultSerializer(true);
       /* redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());*/
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }


}
