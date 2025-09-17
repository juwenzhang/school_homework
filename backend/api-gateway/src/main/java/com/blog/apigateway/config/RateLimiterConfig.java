package com.blog.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    /**
     * 用户维度的限流，使用请求中的用户标识作为key
     */
    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("User-Id"))
                .defaultIfEmpty("anonymous");
    }

    /**
     * IP维度的限流，使用请求的IP地址作为key
     */
    @Bean
    KeyResolver ipKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getRemoteAddress())
                .map(address -> address.getAddress().getHostAddress())
                .defaultIfEmpty("unknown");
    }

    /**
     * 路径维度的限流，使用请求的路径作为key
     */
    @Bean
    KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }
}