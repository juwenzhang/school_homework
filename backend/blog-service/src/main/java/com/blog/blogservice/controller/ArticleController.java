package com.blog.blogservice.controller;

import com.blog.blogservice.entity.Article;
import com.blog.blogservice.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章控制器
 */
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 创建文章
     */
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article createdArticle = articleService.createArticle(article);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    /**
     * 更新文章
     */
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        Article updatedArticle = articleService.updateArticle(id, article);
        return ResponseEntity.ok(updatedArticle);
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取文章详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        // 更新阅读量
        articleService.updateViewCount(id);
        return ResponseEntity.ok(article);
    }

    /**
     * 获取用户的文章列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Article>> getArticlesByUserId(@PathVariable Long userId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedTime"));
        Page<Article> articles = articleService.getArticlesByUserId(userId, pageable);
        return ResponseEntity.ok(articles);
    }

    /**
     * 获取分类下的文章列表
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Article>> getArticlesByCategoryId(@PathVariable Long categoryId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedTime"));
        Page<Article> articles = articleService.getArticlesByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(articles);
    }

    /**
     * 获取热门文章
     */
    @GetMapping("/hot")
    public ResponseEntity<Page<Article>> getHotArticles(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articles = articleService.getHotArticles(pageable);
        return ResponseEntity.ok(articles);
    }

    /**
     * 获取推荐文章
     */
    @GetMapping("/recommend")
    public ResponseEntity<Page<Article>> getRecommendArticles(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedTime"));
        Page<Article> articles = articleService.getRecommendArticles(pageable);
        return ResponseEntity.ok(articles);
    }

    /**
     * 获取置顶文章
     */
    @GetMapping("/top")
    public ResponseEntity<List<Article>> getTopArticles() {
        List<Article> articles = articleService.getTopArticles();
        return ResponseEntity.ok(articles);
    }

    /**
     * 搜索文章
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Article>> searchArticles(@RequestParam String keyword,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedTime"));
        Page<Article> articles = articleService.searchArticles(keyword, pageable);
        return ResponseEntity.ok(articles);
    }

    /**
     * 统计用户文章数量
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countArticlesByUserId(@PathVariable Long userId) {
        Long count = articleService.countArticlesByUserId(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * 点赞文章
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeArticle(@PathVariable Long id) {
        articleService.likeArticle(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 发布文章
     */
    @PostMapping("/{id}/publish")
    public ResponseEntity<Article> publishArticle(@PathVariable Long id) {
        Article article = articleService.publishArticle(id);
        return ResponseEntity.ok(article);
    }

    /**
     * 下架文章
     */
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<Article> unpublishArticle(@PathVariable Long id) {
        Article article = articleService.unpublishArticle(id);
        return ResponseEntity.ok(article);
    }
}