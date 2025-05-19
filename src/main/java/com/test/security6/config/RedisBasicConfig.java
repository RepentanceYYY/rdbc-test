package com.test.security6.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisBasicConfig {
    /**
     * 自定义key规则
     *
     * @return
     */
    /**
     * 自定义key规则
     *
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    @Bean(name = "redisTemplateDb0")
    public RedisTemplate<String, Object> redisTemplateDb0(RedisConnectionFactory factory) {
        return createRedisTemplate(factory, 0);
    }

    @Bean(name = "redisTemplateDb1")
    public RedisTemplate<String, Object> redisTemplateDb1(RedisConnectionFactory factory) {
        return createRedisTemplate(factory, 1);
    }
    @Bean(name = "redisTemplateDb2")
    public RedisTemplate<String, Object> redisTemplateDb2(RedisConnectionFactory factory) {
        return createRedisTemplate(factory, 2);
    }


    private RedisTemplate<String, Object> createRedisTemplate(RedisConnectionFactory factory, int dbIndex) {
        LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory) factory;
        /*复用ClientResources避免新疆连接导致连接池问题*/
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(lettuceConnectionFactory.getHostName());
        configuration.setPort(lettuceConnectionFactory.getPort());
        configuration.setPassword(lettuceConnectionFactory.getPassword());
        configuration.setDatabase(dbIndex);
        //复用资源
        LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration
                .builder()
                .clientResources(lettuceConnectionFactory.getClientResources())
                .commandTimeout(Duration.ofMillis(lettuceConnectionFactory.getTimeout())).build();
        //初始化连接工厂
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration, clientConfiguration);
        connectionFactory.afterPropertiesSet();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        configureSerializer(redisTemplate);

        return redisTemplate;
    }

    private void configureSerializer(RedisTemplate<String, Object> redisTemplate) {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(om, Object.class);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
    }

    /**
     * 设置CacheManager缓存规则
     *
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(om, Object.class);

        // Redis 缓存配置，过期时间300秒
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(300))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .disableCachingNullValues();

        // 直接使用Spring Boot自动注入的factory
        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }
}
