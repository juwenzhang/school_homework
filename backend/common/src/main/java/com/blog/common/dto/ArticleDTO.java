package com.blog.common.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章DTO类
 */
@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private Integer status;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer isTop;
    private Integer allowComment;
    private UserDTO author;
    private CategoryDTO category;
    private List<TagDTO> tags;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private LocalDateTime publishedTime;
}