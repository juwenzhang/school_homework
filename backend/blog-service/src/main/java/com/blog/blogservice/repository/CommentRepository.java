package com.blog.blogservice.repository;

import com.blog.blogservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论仓库接口
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 根据文章ID查询评论
     * @param articleId 文章ID
     * @param status 评论状态
     * @param pageable 分页参数
     * @return 评论列表
     */
    Page<Comment> findByArticleIdAndStatus(Long articleId, Integer status, Pageable pageable);

    /**
     * 根据父评论ID查询子评论
     * @param parentId 父评论ID
     * @param status 评论状态
     * @return 子评论列表
     */
    List<Comment> findByParentIdAndStatus(Long parentId, Integer status);

    /**
     * 根据用户ID查询评论
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 评论列表
     */
    Page<Comment> findByUserId(Long userId, Pageable pageable);

    /**
     * 统计文章的评论数量
     * @param articleId 文章ID
     * @param status 评论状态
     * @return 评论数量
     */
    Long countByArticleIdAndStatus(Long articleId, Integer status);
}