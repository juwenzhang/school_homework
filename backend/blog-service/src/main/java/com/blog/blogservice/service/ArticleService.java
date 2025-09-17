package com.blog.blogservice.service;

import com.blog.blogservice.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 文章服务接口
 */
public interface ArticleService {

    /**
     * 创建文章
     * @param article 文章信息
     * @return 创建的文章
     */
    Article createArticle(Article article);

    /**
     * 更新文章
     * @param id 文章ID
     * @param article 文章信息
     * @return 更新后的文章
     */
    Article updateArticle(Long id, Article article);

    /**
     * 删除文章
     * @param id 文章ID
     */
    void deleteArticle(Long id);

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    Article getArticleById(Long id);

    /**
     * 根据用户ID获取文章列表
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 文章列表
     */
    Page<Article> getArticlesByUserId(Long userId, Pageable pageable);

    /**
     * 根据分类ID获取文章列表
     * @param categoryId 分类ID
     * @param pageable 分页参数
     * @return 文章列表
     */
    Page<Article> getArticlesByCategoryId(Long categoryId, Pageable pageable);

    /**
     * 获取热门文章
     * @param pageable 分页参数
     * @return 热门文章列表
     */
    Page<Article> getHotArticles(Pageable pageable);

    /**
     * 获取推荐文章
     * @param pageable 分页参数
     * @return 推荐文章列表
     */
    Page<Article> getRecommendArticles(Pageable pageable);

    /**
     * 获取置顶文章
     * @return 置顶文章列表
     */
    List<Article> getTopArticles();

    /**
     * 搜索文章
     * @param keyword 搜索关键词
     * @param pageable 分页参数
     * @return 文章列表
     */
    Page<Article> searchArticles(String keyword, Pageable pageable);

    /**
     * 统计文章数量
     * @param userId 用户ID
     * @return 文章数量
     */
    Long countArticlesByUserId(Long userId);

    /**
     * 更新文章阅读量
     * @param id 文章ID
     */
    void updateViewCount(Long id);

    /**
     * 点赞文章
     * @param id 文章ID
     */
    void likeArticle(Long id);

    /**
     * 发布文章
     * @param id 文章ID
     */
    Article publishArticle(Long id);

    /**
     * 下架文章
     * @param id 文章ID
     */
    Article unpublishArticle(Long id);
}