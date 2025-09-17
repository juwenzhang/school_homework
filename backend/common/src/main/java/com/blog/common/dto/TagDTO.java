package com.blog.common.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 标签DTO类
 */
@Data
public class TagDTO {
    private Long id;
    private String name;
    private Integer articleCount;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}