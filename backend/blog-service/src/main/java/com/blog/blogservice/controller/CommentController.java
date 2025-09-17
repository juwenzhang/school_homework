package com.blog.blogservice.controller;

import com.blog.blogservice.entity.Comment;
import com.blog.blogservice.service.CommentService;
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
 * 评论控制器
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 创建评论
     */
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    /**
     * 更新评论
     */
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        Comment updatedComment = commentService.updateComment(id, comment);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取评论详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    /**
     * 获取文章的评论列表
     */
    @GetMapping("/article/{articleId}")
    public ResponseEntity<Page<Comment>> getCommentsByArticleId(@PathVariable Long articleId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdTime"));
        Page<Comment> comments = commentService.getCommentsByArticleId(articleId, pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * 获取父评论的子评论
     */
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Comment>> getCommentsByParentId(@PathVariable Long parentId) {
        List<Comment> comments = commentService.getCommentsByParentId(parentId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 获取用户的评论列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Comment>> getCommentsByUserId(@PathVariable Long userId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<Comment> comments = commentService.getCommentsByUserId(userId, pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * 审核评论
     */
    @PostMapping("/{id}/audit")
    public ResponseEntity<Comment> auditComment(@PathVariable Long id, @RequestParam Integer status) {
        Comment comment = commentService.auditComment(id, status);
        return ResponseEntity.ok(comment);
    }

    /**
     * 点赞评论
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long id) {
        commentService.likeComment(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 统计文章的评论数量
     */
    @GetMapping("/article/{articleId}/count")
    public ResponseEntity<Long> countCommentsByArticleId(@PathVariable Long articleId) {
        Long count = commentService.countCommentsByArticleId(articleId);
        return ResponseEntity.ok(count);
    }
}