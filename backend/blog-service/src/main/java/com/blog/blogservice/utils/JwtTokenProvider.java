package com.blog.blogservice.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT令牌提供者 
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    /**
     * 从令牌中获取用户名 - 简化实现
     */
    public String getUsernameFromToken(String token) {
        try {
            // 从JWT中提取用户名
            String[] parts = token.split("\\.");
            if (parts.length >= 2) {
                String payload = new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8);
                int start = payload.indexOf("\"sub\":\"") + 8;
                int end = payload.indexOf('"', start);
                if (start > 0 && end > start) {
                    return payload.substring(start, end);
                }
            }
        } catch (Exception _) {}
        return null;
    }

    /**
     * 检查令牌是否过期
     */
    private Boolean isTokenExpired(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length >= 2) {
                String payload = new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8);
                int start = payload.indexOf("\"exp\":") + 7;
                int end = payload.indexOf(',', start);
                if (end == -1) {
                    end = payload.indexOf('}', start);
                }
                if (start > 0 && end > start) {
                    long exp = Long.parseLong(payload.substring(start, end));
                    return exp < new Date().getTime() / 1000;
                }
            }
        } catch (Exception _) {}
        return true;
    }

    /**
     * 生成令牌 
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateToken(userPrincipal.getUsername());
    }

    /**
     * 生成令牌 
     */
    public String generateToken(String username) {
        // 这里返回一个简单的令牌格式，不进行实际签名
        // 在生产环境中，应该使用标准的JWT库进行签名
        long now = System.currentTimeMillis();
        long exp = now + jwtExpirationInMs;
        
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = String.format("{\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}", username, now / 1000, exp / 1000);
        
        String encodedHeader = Base64.getEncoder().encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        
        // 这里不进行真正的签名，只是返回一个格式上的JWT
        // 在实际应用中，应该使用标准的JWT库进行签名
        return encodedHeader + "." + encodedPayload + ".signature";
    }

    /**
     * 验证令牌
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 刷新令牌 
     */
    public String refreshToken(String token) {
        String username = getUsernameFromToken(token);
        if (username != null) {
            return generateToken(username);
        }
        return null;
    }
}