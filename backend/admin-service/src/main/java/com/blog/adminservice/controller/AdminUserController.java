package com.blog.adminservice.controller;

import com.blog.adminservice.entity.AdminUser;
import com.blog.adminservice.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Autowired
    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    /**
     * 创建管理员用户
     */
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AdminUser> createAdminUser(@RequestBody AdminUser adminUser) {
        AdminUser createdAdminUser = adminUserService.createAdminUser(adminUser);
        return new ResponseEntity<>(createdAdminUser, HttpStatus.CREATED);
    }

    /**
     * 更新管理员用户信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<AdminUser> updateAdminUser(@PathVariable Long id, @RequestBody AdminUser adminUser) {
        AdminUser updatedAdminUser = adminUserService.updateAdminUser(id, adminUser);
        return ResponseEntity.ok(updatedAdminUser);
    }

    /**
     * 获取管理员用户列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<AdminUser>> getAdminUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        Page<AdminUser> adminUsers = adminUserService.findAllAdminUsers(pageable);
        return ResponseEntity.ok(adminUsers);
    }

    /**
     * 根据ID获取管理员用户
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<AdminUser> getAdminUserById(@PathVariable Long id) {
        return adminUserService.findAdminUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 启用/禁用管理员用户
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('SUPER_ADMIN') and #id != authentication.principal.id")
    public ResponseEntity<AdminUser> enableOrDisableAdminUser(
            @PathVariable Long id,
            @RequestParam boolean enabled) {
        
        AdminUser updatedAdminUser = adminUserService.enableOrDisableAdminUser(id, enabled);
        return ResponseEntity.ok(updatedAdminUser);
    }

    /**
     * 重置管理员用户密码
     */
    @PatchMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AdminUser> resetAdminUserPassword(
            @PathVariable Long id,
            @RequestBody String newPassword) {
        
        AdminUser updatedAdminUser = adminUserService.resetAdminUserPassword(id, newPassword);
        return ResponseEntity.ok(updatedAdminUser);
    }

    /**
     * 删除管理员用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') and #id != authentication.principal.id")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable Long id) {
        adminUserService.deleteAdminUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取管理员用户总数
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Long> getAdminUserCount() {
        long count = adminUserService.countAdminUsers();
        return ResponseEntity.ok(count);
    }

    /**
     * 搜索管理员用户
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<AdminUser>> searchAdminUsers(
            @RequestParam String keyword) {
        
        List<AdminUser> adminUsers = adminUserService.searchAdminUsers(keyword);
        return ResponseEntity.ok(adminUsers);
    }
}