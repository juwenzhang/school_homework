package com.blog.common.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
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
     * 评论内容
     */
    @Column(nullable = false, length = 1000)
    private String content;

    /**
     * 评论用户
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 被评论文章
     */
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 评论状态：0-待审核，1-已通过，2-已拒绝
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 点赞数
     */
    private Integer likeCount = 0;

    /**
     * 回复列表
     */
    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
    private List<Comment> replies;

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