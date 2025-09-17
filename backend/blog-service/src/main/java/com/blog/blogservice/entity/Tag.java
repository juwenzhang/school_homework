package com.blog.blogservice.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签实体类
 */
@Data
@Entity
@Table(name = "blog_tag")
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标签名称
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * 标签描述
     */
    @Column(length = 255)
    private String description;

    /**
     * 标签颜色
     */
    @Column(length = 20)
    private String color;

    /**
     * 标签排序
     */
    private Integer sort = 0;

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