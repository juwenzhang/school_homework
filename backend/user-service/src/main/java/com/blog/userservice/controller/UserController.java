package com.blog.userservice.controller;

import com.blog.common.dto.PageQueryDTO;
import com.blog.common.dto.ResponseDTO;
import com.blog.common.dto.UserDTO;
import com.blog.common.exception.UserException;
import com.blog.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current")
    public ResponseDTO<UserDTO> getCurrentUser() {
        // 从Security上下文中获取当前认证用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 根据用户名查询用户信息
            UserDTO userDTO = userService.getUserByUsername(userDetails.getUsername());
            return ResponseDTO.success(userDTO);
        }
        throw new UserException("用户未登录");
    }

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/{id}")
    public ResponseDTO<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseDTO.success(userDTO);
    }

    /**
     * 获取用户列表
     */
    @GetMapping
    public ResponseDTO<?> listUsers(PageQueryDTO pageQueryDTO) {
        return ResponseDTO.success(userService.listUsers(pageQueryDTO));
    }

    /**
     * 创建用户
     */
    @PostMapping
    public ResponseDTO<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseDTO.success(createdUser);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseDTO<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseDTO.success(updatedUser);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseDTO<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseDTO.success();
    }

    /**
     * 修改用户状态
     */
    @PutMapping("/{id}/status")
    public ResponseDTO<Void> changeUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.changeUserStatus(id, status);
        return ResponseDTO.success();
    }

    /**
     * 为用户分配角色
     */
    @PutMapping("/{id}/roles")
    public ResponseDTO<Void> assignRolesToUser(@PathVariable Long id, @RequestBody Long[] roleIds) {
        userService.assignRolesToUser(id, roleIds);
        return ResponseDTO.success();
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/reset-password")
    public ResponseDTO<Void> resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return ResponseDTO.success();
    }

    /**
     * 用户修改密码
     */
    @PutMapping("/change-password")
    public ResponseDTO<Void> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        // 从Security上下文中获取当前认证用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 根据用户名查询用户信息
            UserDTO userDTO = userService.getUserByUsername(userDetails.getUsername());
            userService.changePassword(userDTO.getId(), oldPassword, newPassword);
            return ResponseDTO.success();
        }
        throw new UserException("用户未登录");
    }
}