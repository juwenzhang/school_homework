package com.blog.common.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限DTO类
 */
@Data
public class PermissionDTO {
    private Long id;
    private String permissionName;
    private String permissionCode;
    private String description;
    private Integer type;
    private Long parentId;
    private String path;
    private String component;
    private String icon;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<PermissionDTO> children;
}