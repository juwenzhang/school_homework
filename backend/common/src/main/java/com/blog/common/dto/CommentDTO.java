package com.blog.common.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论DTO类
 */
@Data
public class CommentDTO {
    private Long id;
    private String content;
    private UserDTO user;
    private ArticleDTO article;
    private Long parentId;
    private Integer status;
    private Integer likeCount;
    private List<CommentDTO> replies;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}