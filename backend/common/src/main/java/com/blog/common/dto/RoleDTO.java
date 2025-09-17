package com.blog.common.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色DTO类
 */
@Data
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private List<PermissionDTO> permissions;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}