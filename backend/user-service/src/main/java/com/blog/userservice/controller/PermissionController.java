package com.blog.userservice.controller;

import com.blog.common.dto.PageQueryDTO;
import com.blog.common.dto.PageResultDTO;
import com.blog.common.dto.PermissionDTO;
import com.blog.common.dto.ResponseDTO;
import com.blog.userservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 根据ID获取权限信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:view')")
    public ResponseDTO<PermissionDTO> getPermissionById(@PathVariable Long id) {
        PermissionDTO permissionDTO = permissionService.getPermissionById(id);
        return ResponseDTO.success(permissionDTO);
    }

    /**
     * 根据名称获取权限信息
     */
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('permission:view')")
    public ResponseDTO<PermissionDTO> getPermissionByName(@PathVariable String name) {
        PermissionDTO permissionDTO = permissionService.getPermissionByName(name);
        return ResponseDTO.success(permissionDTO);
    }

    /**
     * 根据编码获取权限信息
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAuthority('permission:view')")
    public ResponseDTO<PermissionDTO> getPermissionByCode(@PathVariable String code) {
        PermissionDTO permissionDTO = permissionService.getPermissionByCode(code);
        return ResponseDTO.success(permissionDTO);
    }

    /**
     * 分页查询权限列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('permission:list')")
    public ResponseDTO<PageResultDTO<PermissionDTO>> listPermissions(PageQueryDTO pageQueryDTO) {
        PageResultDTO<PermissionDTO> pageResult = permissionService.listPermissions(pageQueryDTO);
        return ResponseDTO.success(pageResult);
    }

    /**
     * 获取所有权限列表
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('permission:list')")
    public ResponseDTO<List<PermissionDTO>> getAllPermissions() {
        List<PermissionDTO> permissionDTOList = permissionService.getAllPermissions();
        return ResponseDTO.success(permissionDTOList);
    }

    /**
     * 根据权限类型查询权限列表
     */
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAuthority('permission:list')")
    public ResponseDTO<List<PermissionDTO>> getPermissionsByType(@PathVariable Integer type) {
        List<PermissionDTO> permissionDTOList = permissionService.getPermissionsByType(type);
        return ResponseDTO.success(permissionDTOList);
    }

    /**
     * 根据父权限ID查询子权限列表
     */
    @GetMapping("/parent/{parentId}")
    @PreAuthorize("hasAuthority('permission:list')")
    public ResponseDTO<List<PermissionDTO>> getPermissionsByParentId(@PathVariable Long parentId) {
        List<PermissionDTO> permissionDTOList = permissionService.getPermissionsByParentId(parentId);
        return ResponseDTO.success(permissionDTOList);
    }

    /**
     * 获取权限树
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('permission:list')")
    public ResponseDTO<List<PermissionDTO>> getPermissionTree() {
        List<PermissionDTO> permissions = permissionService.getAllPermissions();
        List<PermissionDTO> permissionTree = permissionService.buildPermissionTree(permissions);
        return ResponseDTO.success(permissionTree);
    }

    /**
     * 创建权限
     */
    @PostMapping
    @PreAuthorize("hasAuthority('permission:create')")
    public ResponseDTO<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
        PermissionDTO createdPermission = permissionService.createPermission(permissionDTO);
        return ResponseDTO.success(createdPermission);
    }

    /**
     * 更新权限信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:update')")
    public ResponseDTO<PermissionDTO> updatePermission(@PathVariable Long id, @RequestBody PermissionDTO permissionDTO) {
        PermissionDTO updatedPermission = permissionService.updatePermission(id, permissionDTO);
        return ResponseDTO.success(updatedPermission);
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:delete')")
    public ResponseDTO<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseDTO.success();
    }

    /**
     * 批量删除权限
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('permission:delete')")
    public ResponseDTO<Void> deletePermissions(@RequestBody Long[] ids) {
        permissionService.deletePermissions(ids);
        return ResponseDTO.success();
    }

    /**
     * 修改权限状态
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('permission:update')")
    public ResponseDTO<Void> changePermissionStatus(@PathVariable Long id, @RequestParam Integer status) {
        permissionService.changePermissionStatus(id, status);
        return ResponseDTO.success();
    }
}