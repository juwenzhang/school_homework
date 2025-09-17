package com.blog.blogservice.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类实体类
 */
@Data
@Entity
@Table(name = "blog_category")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分类名称
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * 分类描述
     */
    @Column(length = 255)
    private String description;

    /**
     * 父分类ID
     */
    private Long parentId = 0L;

    /**
     * 分类图标
     */
    @Column(length = 100)
    private String icon;

    /**
     * 分类排序
     */
    private Integer sort = 0;

    /**
     * 分类状态
     * 1: 启用, 0: 禁用
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 文章数量
     */
    private Integer articleCount = 0;

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