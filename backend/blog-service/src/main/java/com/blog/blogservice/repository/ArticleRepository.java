package com.blog.blogservice.repository;

import com.blog.blogservice.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 文章仓库接口
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * 根据用户ID查询文章
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 文章列表
     */
    Page<Article> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据分类ID查询文章
     * @param categoryId 分类ID
     * @param pageable 分页参数
     * @return 文章列表
     */
    Page<Article> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * 根据状态查询文章
     * @param status 文章状态
     * @param pageable 分页参数
     * @return 文章列表
     */
    Page<Article> findByStatus(Integer status, Pageable pageable);

    /**
     * 查询热门文章
     * @param pageable 分页参数
     * @return 热门文章列表
     */
    Page<Article> findByStatusOrderByViewCountDesc(Integer status, Pageable pageable);

    /**
     * 查询推荐文章
     * @param isRecommend 是否推荐
     * @param status 文章状态
     * @param pageable 分页参数
     * @return 推荐文章列表
     */
    Page<Article> findByIsRecommendAndStatus(Boolean isRecommend, Integer status, Pageable pageable);

    /**
     * 查询置顶文章
     * @param isTop 是否置顶
     * @param status 文章状态
     * @return 置顶文章列表
     */
    List<Article> findByIsTopAndStatusOrderByUpdatedTimeDesc(Boolean isTop, Integer status);

    /**
     * 根据标题模糊查询文章
     * @param title 文章标题
     * @param status 文章状态
     * @param pageable 分页参数
     * @return 文章列表
     */
    Page<Article> findByTitleContainingAndStatus(String title, Integer status, Pageable pageable);

    /**
     * 统计用户发布的文章数量
     * @param userId 用户ID
     * @param status 文章状态
     * @return 文章数量
     */
    Long countByUserIdAndStatus(Long userId, Integer status);
}