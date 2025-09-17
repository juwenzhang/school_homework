package com.blog.adminservice.repository;

import com.blog.adminservice.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    /**
     * 根据用户名查询管理员用户
     */
    Optional<AdminUser> findByUsername(String username);

    /**
     * 根据邮箱查询管理员用户
     */
    Optional<AdminUser> findByEmail(String email);

    /**
     * 检查用户名是否已存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     */
    boolean existsByEmail(String email);

    /**
     * 统计管理员用户数量
     */
    @Query("SELECT COUNT(a) FROM AdminUser a")
    long countAllAdminUsers();

    /**
     * 根据用户名或邮箱模糊搜索
     */
    @Query("SELECT a FROM AdminUser a WHERE a.username LIKE %:keyword% OR a.email LIKE %:keyword%")
    Iterable<AdminUser> searchByUsernameOrEmail(@Param("keyword") String keyword);
}