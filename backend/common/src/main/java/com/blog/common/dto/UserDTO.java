package com.blog.common.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户DTO类
 */
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String avatar;
    private String bio;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime lastLoginTime;
    private String phoneNumber;
    private Integer gender;
    private List<Long> roleIds;
    private List<String> roleNames;
    private List<String> permissions;
}