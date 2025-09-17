package com.blog.userservice.repository;

import com.blog.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户仓库接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    Boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 是否存在
     */
    Boolean existsByEmail(String email);

    /**
     * 根据用户名或邮箱查询用户
     * @param username 用户名
     * @param email 邮箱
     * @return 用户信息
     */
    @Query("SELECT u FROM User u WHERE u.username = ?1 OR u.email = ?2")
    Optional<User> findByUsernameOrEmail(String username, String email);
}