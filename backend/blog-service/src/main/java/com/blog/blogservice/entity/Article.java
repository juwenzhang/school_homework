package com.blog.blogservice.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 文章实体类
 */
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
    @Column(columnDefinition = "text")
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

    /**
     * 标签列表
     */
    @ManyToMany
    @JoinTable(name = "blog_article_tag",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    public Boolean getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Boolean isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public LocalDateTime getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(LocalDateTime publishedTime) {
        this.publishedTime = publishedTime;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    // toString method
    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", status=" + status +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                ", commentCount=" + commentCount +
                ", isTop=" + isTop +
                ", isRecommend=" + isRecommend +
                ", allowComment=" + allowComment +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", publishedTime=" + publishedTime +
                ", tags.size=" + (tags != null ? tags.size() : 0) +
                '}';
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}