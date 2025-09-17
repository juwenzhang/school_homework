package com.blog.adminservice.service;

import com.blog.adminservice.entity.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdminUserService {

    /**
     * 创建管理员用户
     */
    AdminUser createAdminUser(AdminUser adminUser);

    /**
     * 更新管理员用户信息
     */
    AdminUser updateAdminUser(Long id, AdminUser adminUser);

    /**
     * 查询管理员用户列表
     */
    Page<AdminUser> findAllAdminUsers(Pageable pageable);

    /**
     * 根据ID查询管理员用户
     */
    Optional<AdminUser> findAdminUserById(Long id);

    /**
     * 根据用户名查询管理员用户
     */
    Optional<AdminUser> findAdminUserByUsername(String username);

    /**
     * 启用/禁用管理员用户
     */
    AdminUser enableOrDisableAdminUser(Long id, boolean enabled);

    /**
     * 重置管理员用户密码
     */
    AdminUser resetAdminUserPassword(Long id, String newPassword);

    /**
     * 删除管理员用户
     */
    void deleteAdminUser(Long id);

    /**
     * 统计管理员用户数量
     */
    long countAdminUsers();

    /**
     * 根据关键词搜索管理员用户
     */
    List<AdminUser> searchAdminUsers(String keyword);

    /**
     * 检查用户名是否已存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     */
    boolean existsByEmail(String email);
}