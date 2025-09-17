package com.blog.common.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 文章实体类
 */
@Data
@Entity
@Table(name = "blog_article")
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章标题
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 文章内容
     */
    @Lob
    @Column(nullable = false)
    private String content;

    /**
     * 文章摘要
     */
    @Column(length = 500)
    private String summary;

    /**
     * 文章封面
     */
    @Column(length = 255)
    private String coverImage;

    /**
     * 作者ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 分类ID
     */
    @Column(nullable = false)
    private Long categoryId;

    /**
     * 文章状态
     * 1: 草稿, 2: 已发布, 3: 已下架
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 阅读量
     */
    private Long viewCount = 0L;

    /**
     * 点赞量
     */
    private Long likeCount = 0L;

    /**
     * 评论量
     */
    private Long commentCount = 0L;

    /**
     * 是否置顶
     */
    private Boolean isTop = false;

    /**
     * 是否推荐
     */
    private Boolean isRecommend = false;

    /**
     * 是否允许评论
     */
    private Boolean allowComment = true;

    /**
     * 标签列表
     */
    @ManyToMany
    @JoinTable(name = "blog_article_tag",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

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
     * 发布时间
     */
    private LocalDateTime publishedTime;
}