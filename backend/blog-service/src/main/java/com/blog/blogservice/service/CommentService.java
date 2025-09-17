package com.blog.blogservice.service;

import com.blog.blogservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService {

    /**
     * 创建评论
     * @param comment 评论信息
     * @return 创建的评论
     */
    Comment createComment(Comment comment);

    /**
     * 更新评论
     * @param id 评论ID
     * @param comment 评论信息
     * @return 更新后的评论
     */
    Comment updateComment(Long id, Comment comment);

    /**
     * 删除评论
     * @param id 评论ID
     */
    void deleteComment(Long id);

    /**
     * 获取评论详情
     * @param id 评论ID
     * @return 评论详情
     */
    Comment getCommentById(Long id);

    /**
     * 根据文章ID获取评论列表
     * @param articleId 文章ID
     * @param pageable 分页参数
     * @return 评论列表
     */
    Page<Comment> getCommentsByArticleId(Long articleId, Pageable pageable);

    /**
     * 根据父评论ID获取子评论
     * @param parentId 父评论ID
     * @return 子评论列表
     */
    List<Comment> getCommentsByParentId(Long parentId);

    /**
     * 根据用户ID获取评论列表
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 评论列表
     */
    Page<Comment> getCommentsByUserId(Long userId, Pageable pageable);

    /**
     * 审核评论
     * @param id 评论ID
     * @param status 评论状态
     * @return 审核后的评论
     */
    Comment auditComment(Long id, Integer status);

    /**
     * 点赞评论
     * @param id 评论ID
     */
    void likeComment(Long id);

    /**
     * 统计文章的评论数量
     * @param articleId 文章ID
     * @return 评论数量
     */
    Long countCommentsByArticleId(Long articleId);
}