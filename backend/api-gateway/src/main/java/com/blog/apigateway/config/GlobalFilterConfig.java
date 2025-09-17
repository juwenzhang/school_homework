package com.blog.apigateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class GlobalFilterConfig implements GlobalFilter, Ordered {

    private static final String TRACE_ID = "traceId";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 生成唯一的traceId
        String traceId = UUID.randomUUID().toString();
        
        // 将traceId添加到请求头中
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(TRACE_ID, traceId)
                .build();
        
        // 记录请求信息
        logRequest(request, traceId);
        
        // 修改exchange的request
        ServerWebExchange modifiedExchange = exchange.mutate().request(request).build();
        
        // 执行过滤链
        return chain.filter(modifiedExchange).then(Mono.fromRunnable(() -> {
            // 记录响应信息
            logResponse(modifiedExchange, traceId);
        }));
    }

    private void logRequest(ServerHttpRequest request, String traceId) {
        String timestamp = LocalDateTime.now().format(formatter);
        String method = request.getMethodValue();
        String path = request.getURI().getPath();
        String remoteAddress = request.getRemoteAddress() != null ? request.getRemoteAddress().toString() : "unknown";
        
        System.out.printf("[%s] [TRACE_ID: %s] Request: %s %s from %s\n", 
                timestamp, traceId, method, path, remoteAddress);
    }

    private void logResponse(ServerWebExchange exchange, String traceId) {
        String timestamp = LocalDateTime.now().format(formatter);
        int statusCode = exchange.getResponse().getStatusCode() != null ? 
                exchange.getResponse().getStatusCode().value() : 0;
                
        System.out.printf("[%s] [TRACE_ID: %s] Response: status=%d\n", 
                timestamp, traceId, statusCode);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}