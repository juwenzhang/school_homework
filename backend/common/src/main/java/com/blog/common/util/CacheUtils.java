package com.blog.common.util;

import com.blog.common.constant.SystemConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 */
@Component
public class CacheUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存并指定过期时间
     * @param key 缓存键
     * @param value 缓存值
     * @param expireTime 过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    /**
     * 设置缓存并使用默认过期时间（1小时）
     * @param key 缓存键
     * @param value 缓存值
     */
    public void setWithDefaultExpire(String key, Object value) {
        set(key, value, SystemConstants.CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存
     * @param key 缓存键
     * @return 缓存值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     * @param key 缓存键
     * @return 是否删除成功
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 判断缓存是否存在
     * @param key 缓存键
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 递增
     * @param key 缓存键
     * @return 递增后的值
     */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 递减
     * @param key 缓存键
     * @return 递减后的值
     */
    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 设置过期时间
     * @param key 缓存键
     * @param expireTime 过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    public Boolean expire(String key, long expireTime, TimeUnit timeUnit) {
        return redisTemplate.expire(key, expireTime, timeUnit);
    }

    /**
     * 获取缓存过期时间
     * @param key 缓存键
     * @param timeUnit 时间单位
     * @return 过期时间
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 根据前缀删除缓存
     * @param prefix 缓存键前缀
     */
    public void deleteByPrefix(String prefix) {
        redisTemplate.delete(redisTemplate.keys(prefix + "*"));
    }

    /**
     * 构建用户缓存键
     * @param userId 用户ID
     * @return 缓存键
     */
    public static String buildUserCacheKey(Long userId) {
        return SystemConstants.CACHE_KEY_USER + userId;
    }

    /**
     * 构建文章缓存键
     * @param articleId 文章ID
     * @return 缓存键
     */
    public static String buildArticleCacheKey(Long articleId) {
        return SystemConstants.CACHE_KEY_ARTICLE + articleId;
    }

    /**
     * 构建分类缓存键
     * @param categoryId 分类ID
     * @return 缓存键
     */
    public static String buildCategoryCacheKey(Long categoryId) {
        return SystemConstants.CACHE_KEY_CATEGORY + categoryId;
    }

    /**
     * 构建标签缓存键
     * @param tagId 标签ID
     * @return 缓存键
     */
    public static String buildTagCacheKey(Long tagId) {
        return SystemConstants.CACHE_KEY_TAG + tagId;
    }

    /**
     * 构建权限缓存键
     * @param permissionId 权限ID
     * @return 缓存键
     */
    public static String buildPermissionCacheKey(Long permissionId) {
        return SystemConstants.CACHE_KEY_PERMISSION + permissionId;
    }

    /**
     * 构建角色缓存键
     * @param roleId 角色ID
     * @return 缓存键
     */
    public static String buildRoleCacheKey(Long roleId) {
        return SystemConstants.CACHE_KEY_ROLE + roleId;
    }
}