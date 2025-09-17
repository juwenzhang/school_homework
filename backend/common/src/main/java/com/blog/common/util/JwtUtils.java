package com.blog.common.util;

import com.blog.common.constant.SystemConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT工具类
 */
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private static final String SECRET_KEY = SystemConstants.JWT_SECRET_KEY;
    private static final Long EXPIRE_TIME = SystemConstants.JWT_EXPIRE_TIME;

    /**
     * 生成JWT令牌
     * @param userId 用户ID
     * @param username 用户名
     * @param extraClaims 额外的声明信息
     * @return JWT令牌
     */
    public static String generateToken(Long userId, String username, Map<String, Object> extraClaims) {
        Map<String, Object> claims = new ConcurrentHashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        if (extraClaims != null) {
            claims.putAll(extraClaims);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * 生成JWT令牌
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT令牌
     */
    public static String generateToken(Long userId, String username) {
        return generateToken(userId, username, null);
    }

    /**
     * 解析JWT令牌
     * @param token JWT令牌
     * @return 令牌中的声明信息
     */
    public static Claims parseToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("解析JWT令牌失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从JWT令牌中获取用户ID
     * @param token JWT令牌
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return claims.get("userId", Long.class);
    }

    /**
     * 从JWT令牌中获取用户名
     * @param token JWT令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return claims.get("username", String.class);
    }

    /**
     * 从JWT令牌中获取过期时间
     * @param token JWT令牌
     * @return 过期时间
     */
    public static Date getExpirationDateFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return claims.getExpiration();
    }

    /**
     * 验证JWT令牌是否有效
     * @param token JWT令牌
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            if (claims == null) {
                return false;
            }
            // 检查令牌是否过期
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            logger.error("验证JWT令牌失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从请求头中提取JWT令牌
     * @param authHeader 授权请求头
     * @return JWT令牌
     */
    public static String extractTokenFromHeader(String authHeader) {
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(SystemConstants.JWT_TOKEN_PREFIX)) {
            return null;
        }
        return authHeader.substring(SystemConstants.JWT_TOKEN_PREFIX.length());
    }
}