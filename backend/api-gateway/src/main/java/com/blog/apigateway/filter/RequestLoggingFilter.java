package com.blog.apigateway.filter;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 生成请求ID
        String requestId = UUID.randomUUID().toString();
        ServerHttpRequest request = exchange.getRequest();

        // 添加请求ID到请求头
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-Request-Id", requestId)
                .build();

        // 获取请求信息
        String path = request.getPath().value();
        String method = request.getMethod().name();
        String clientIp = getClientIp(request);
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        long startTime = System.currentTimeMillis();

        // 记录请求开始信息
        logger.info("[{}] Request started: {} {} from {}", requestId, method, path, clientIp);
        logger.debug("[{}] Request headers: {}", requestId, request.getHeaders());

        // 记录请求体
        if (logger.isDebugEnabled()) {
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        String requestBody = new String(bytes, StandardCharsets.UTF_8);
                        DataBufferUtils.release(dataBuffer);
                        logger.debug("[{}] Request body: {}", requestId, requestBody);

                        ServerWebExchange newExchange = exchange.mutate().request(mutatedRequest).build();

                        // 记录响应信息
                        return logResponse(newExchange, chain, requestId, path, method, startTime, timestamp);
                    });
        } else {
            // 不需要记录请求体
            return logResponse(exchange.mutate().request(mutatedRequest).build(), chain, requestId, path, method, startTime, timestamp);
        }
    }

    /**
     * 记录响应信息
     */
    private Mono<Void> logResponse(ServerWebExchange exchange, GatewayFilterChain chain, String requestId, 
                                 String path, String method, long startTime, String timestamp) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        AtomicReference<String> responseBodyRef = new AtomicReference<>();

        // 装饰响应对象以捕获响应体
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux && logger.isDebugEnabled()) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(
                            fluxBody.publishOn(Schedulers.parallel())
                                    .doOnNext(dataBuffer -> {
                                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(bytes);
                                        DataBufferUtils.release(dataBuffer);
                                        String responseBody = new String(bytes, StandardCharsets.UTF_8);
                                        responseBodyRef.set(responseBody);
                                    })
                                    .doOnComplete(() -> {
                                        logger.debug("[{}] Response body: {}", requestId, responseBodyRef.get());
                                    })
                    );
                }
                return super.writeWith(body);
            }
        };

        // 执行过滤链并记录响应完成信息
        return chain.filter(exchange.mutate().response(decoratedResponse).build())
                .doFinally(signalType -> {
                    long duration = System.currentTimeMillis() - startTime;
                    int statusCode = originalResponse.getStatusCode() != null ? 
                            originalResponse.getStatusCode().value() : 0;

                    logger.info("[{}] Request completed: {} {} with status code {} in {}ms", 
                            requestId, method, path, statusCode, duration);
                    logger.debug("[{}] Response headers: {}", requestId, originalResponse.getHeaders());
                });
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(ServerHttpRequest request) {
        // 首先检查X-Forwarded-For头
        HttpHeaders headers = request.getHeaders();
        String xForwardedFor = headers.getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // 多个IP地址的情况下，第一个是客户端真实IP
            return xForwardedFor.split(",")[0].trim();
        }

        // 否则获取请求的远程地址
        return request.getRemoteAddress() != null ? 
                request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
    }

    @Override
    public int getOrder() {
        // 设置过滤器顺序，确保在其他过滤器之前执行
        return -200;
    }
}