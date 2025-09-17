package com.blog.common.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 分类DTO类
 */
@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private Integer sort;
    private Integer articleCount;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}