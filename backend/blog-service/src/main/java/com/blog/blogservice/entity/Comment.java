package com.blog.blogservice.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论实体类
 */
@Data
@Entity
@Table(name = "blog_comment")
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章ID
     */
    @Column(nullable = false)
    private Long articleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    @Column(columnDefinition = "text", nullable = false)
    private String content;

    /**
     * 父评论ID
     * 0表示一级评论
     */
    private Long parentId = 0L;

    /**
     * 用户昵称
     */
    @Column(length = 50)
    private String nickname;

    /**
     * 用户邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 用户头像
     */
    @Column(length = 255)
    private String avatar;

    /**
     * 评论状态
     * 1: 正常, 0: 待审核, -1: 已删除
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 点赞量
     */
    private Integer likeCount = 0;

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
     * 回复列表
     */
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> replies;
}