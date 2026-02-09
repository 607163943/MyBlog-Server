package com.my.blog.server.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CaffeineConfig {
    /**
     * 令牌缓存配置
     * @return 令牌缓存对象
     */
    @Bean
    public Cache<String,String> tokenCache() {
        return Caffeine.newBuilder()
                .maximumSize(10)
                // 缓存有效期1天
                .expireAfterWrite(Duration.ofDays(1))
                .build();
    }

    /**
     * 验证码缓存配置
     * @return 验证码缓存对象
     */
    @Bean
    public Cache<String,String> captchaCache() {
        return Caffeine.newBuilder()
                .maximumSize(100)
                // 缓存有效期1分钟
                .expireAfterWrite(Duration.ofMinutes(1))
                .build();
    }
}
