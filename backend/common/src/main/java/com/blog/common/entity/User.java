package com.blog.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 */
@Data
@Entity
@Table(name = "blog_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码
     */
    @Column(nullable = false, length = 100)
    private String password;

    /**
     * 昵称
     */
    @Column(length = 50)
    private String nickname;

    /**
     * 邮箱
     */
    @Column(unique = true, length = 100)
    private String email;

    /**
     * 头像
     */
    @Column(length = 255)
    private String avatar;

    /**
     * 个人简介
     */
    @Column(length = 500)
    private String bio;

    /**
     * 用户状态：0-禁用，1-启用
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 手机号码
     */
    @Column(length = 20)
    private String phoneNumber;

    /**
     * 性别：0-未知, 1-男, 2-女
     */
    private Integer gender;

    /**
     * 是否删除
     */
    @Column(nullable = false)
    private Boolean deleted = false;

    /**
     * 登录次数
     */
    private Integer loginCount = 0;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * IP地址
     */
    @Column(length = 50)
    private String ipAddress;

    /**
     * 用户代理
     */
    @Column(length = 255)
    private String userAgent;

    /**
     * 用户角色
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "blog_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    private LocalDateTime updatedTime;

    /**
     * 添加角色
     */
    public void addRole(Role role) {
        this.roles.add(role);
    }

    /**
     * 移除角色
     */
    public void removeRole(Role role) {
        this.roles.remove(role);
    }
}