package com.blog.blogservice.service.impl;

import com.blog.blogservice.entity.Comment;
import com.blog.blogservice.repository.CommentRepository;
import com.blog.blogservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment createComment(Comment comment) {
        comment.setCreatedTime(LocalDateTime.now());
        comment.setUpdatedTime(LocalDateTime.now());
        comment.setLikeCount(0);
        // 默认状态为待审核
        if (comment.getStatus() == null) {
            comment.setStatus(0);
        }
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(Long id, Comment comment) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment existingComment = optionalComment.get();
            existingComment.setContent(comment.getContent());
            existingComment.setNickname(comment.getNickname());
            existingComment.setEmail(comment.getEmail());
            existingComment.setAvatar(comment.getAvatar());
            existingComment.setUpdatedTime(LocalDateTime.now());
            return commentRepository.save(existingComment);
        }
        throw new RuntimeException("Comment not found");
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        if (commentRepository.existsById(id)) {
            // 先删除子评论
            List<Comment> replies = commentRepository.findByParentIdAndStatus(id, 1);
            if (!replies.isEmpty()) {
                for (Comment reply : replies) {
                    deleteComment(reply.getId());
                }
            }
            commentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    @Override
    public Comment getCommentById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @Override
    public Page<Comment> getCommentsByArticleId(Long articleId, Pageable pageable) {
        // 只查询审核通过的评论
        return commentRepository.findByArticleIdAndStatus(articleId, 1, pageable);
    }

    @Override
    public List<Comment> getCommentsByParentId(Long parentId) {
        // 只查询审核通过的评论
        return commentRepository.findByParentIdAndStatus(parentId, 1);
    }

    @Override
    public Page<Comment> getCommentsByUserId(Long userId, Pageable pageable) {
        return commentRepository.findByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public Comment auditComment(Long id, Integer status) {
        Comment comment = getCommentById(id);
        comment.setStatus(status);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void likeComment(Long id) {
        Comment comment = getCommentById(id);
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
    }

    @Override
    public Long countCommentsByArticleId(Long articleId) {
        // 统计审核通过的评论数量
        return commentRepository.countByArticleIdAndStatus(articleId, 1);
    }
}