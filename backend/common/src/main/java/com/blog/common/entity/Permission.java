package com.blog.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限实体类
 */
@Data
@Entity
@Table(name = "blog_permission")
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 权限名称
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * 权限编码
     */
    @Column(nullable = false, unique = true, length = 100)
    private String code;

    /**
     * 权限类型：menu-菜单，button-按钮，api-接口
     */
    @Column(nullable = false, length = 20)
    private String type;

    /**
     * 访问路径
     */
    private String path;

    /**
     * 父级权限ID
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    private String icon;

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
}