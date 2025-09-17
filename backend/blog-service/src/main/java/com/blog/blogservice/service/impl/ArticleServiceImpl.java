package com.blog.blogservice.service.impl;

import com.blog.blogservice.entity.Article;
import com.blog.blogservice.repository.ArticleRepository;
import com.blog.blogservice.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 文章服务实现类
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    @Transactional
    public Article createArticle(Article article) {
        article.setCreatedTime(LocalDateTime.now());
        article.setUpdatedTime(LocalDateTime.now());
        article.setViewCount(0L);
        article.setLikeCount(0L);
        article.setCommentCount(0L);
        return articleRepository.save(article);
    }

    @Override
    @Transactional
    public Article updateArticle(Long id, Article article) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article existingArticle = optionalArticle.get();
            existingArticle.setTitle(article.getTitle());
            existingArticle.setContent(article.getContent());
            existingArticle.setSummary(article.getSummary());
            existingArticle.setCoverImage(article.getCoverImage());
            existingArticle.setCategoryId(article.getCategoryId());
            existingArticle.setTags(article.getTags());
            existingArticle.setIsTop(article.getIsTop());
            existingArticle.setIsRecommend(article.getIsRecommend());
            existingArticle.setAllowComment(article.getAllowComment());
            existingArticle.setUpdatedTime(LocalDateTime.now());
            return articleRepository.save(existingArticle);
        }
        throw new RuntimeException("Article not found");
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
        } else {
            throw new RuntimeException("Article not found");
        }
    }

    @Override
    public Article getArticleById(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        return optionalArticle.orElseThrow(() -> new RuntimeException("Article not found"));
    }

    @Override
    public Page<Article> getArticlesByUserId(Long userId, Pageable pageable) {
        return articleRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<Article> getArticlesByCategoryId(Long categoryId, Pageable pageable) {
        return articleRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Article> getHotArticles(Pageable pageable) {
        // 只查询已发布的文章
        return articleRepository.findByStatusOrderByViewCountDesc(2, pageable);
    }

    @Override
    public Page<Article> getRecommendArticles(Pageable pageable) {
        // 查询推荐且已发布的文章
        return articleRepository.findByIsRecommendAndStatus(true, 2, pageable);
    }

    @Override
    public List<Article> getTopArticles() {
        // 查询置顶且已发布的文章
        return articleRepository.findByIsTopAndStatusOrderByUpdatedTimeDesc(true, 2);
    }

    @Override
    public Page<Article> searchArticles(String keyword, Pageable pageable) {
        // 根据标题搜索已发布的文章
        return articleRepository.findByTitleContainingAndStatus(keyword, 2, pageable);
    }

    @Override
    public Long countArticlesByUserId(Long userId) {
        // 统计用户已发布的文章数量
        return articleRepository.countByUserIdAndStatus(userId, 2);
    }

    @Override
    @Transactional
    public void updateViewCount(Long id) {
        Article article = getArticleById(id);
        article.setViewCount(article.getViewCount() + 1);
        articleRepository.save(article);
    }

    @Override
    @Transactional
    public void likeArticle(Long id) {
        Article article = getArticleById(id);
        article.setLikeCount(article.getLikeCount() + 1);
        articleRepository.save(article);
    }

    @Override
    @Transactional
    public Article publishArticle(Long id) {
        Article article = getArticleById(id);
        article.setStatus(2); // 已发布状态
        article.setPublishedTime(LocalDateTime.now());
        return articleRepository.save(article);
    }

    @Override
    @Transactional
    public Article unpublishArticle(Long id) {
        Article article = getArticleById(id);
        article.setStatus(3); // 已下架状态
        return articleRepository.save(article);
    }
}