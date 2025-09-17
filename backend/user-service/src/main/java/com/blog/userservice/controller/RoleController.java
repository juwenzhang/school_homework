package com.blog.userservice.controller;

import com.blog.common.dto.PageQueryDTO;
import com.blog.common.dto.PageResultDTO;
import com.blog.common.dto.ResponseDTO;
import com.blog.common.dto.RoleDTO;
import com.blog.userservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 根据ID获取角色信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:view')")
    public ResponseDTO<RoleDTO> getRoleById(@PathVariable Long id) {
        RoleDTO roleDTO = roleService.getRoleById(id);
        return ResponseDTO.success(roleDTO);
    }

    /**
     * 根据名称获取角色信息
     */
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('role:view')")
    public ResponseDTO<RoleDTO> getRoleByName(@PathVariable String name) {
        RoleDTO roleDTO = roleService.getRoleByName(name);
        return ResponseDTO.success(roleDTO);
    }

    /**
     * 分页查询角色列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('role:list')")
    public ResponseDTO<PageResultDTO<RoleDTO>> listRoles(PageQueryDTO pageQueryDTO) {
        PageResultDTO<RoleDTO> pageResult = roleService.listRoles(pageQueryDTO);
        return ResponseDTO.success(pageResult);
    }

    /**
     * 获取所有角色列表
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('role:list')")
    public ResponseDTO<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roleDTOList = roleService.getAllRoles();
        return ResponseDTO.success(roleDTOList);
    }

    /**
     * 创建角色
     */
    @PostMapping
    @PreAuthorize("hasAuthority('role:create')")
    public ResponseDTO<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return ResponseDTO.success(createdRole);
    }

    /**
     * 更新角色信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseDTO<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        RoleDTO updatedRole = roleService.updateRole(id, roleDTO);
        return ResponseDTO.success(updatedRole);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public ResponseDTO<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseDTO.success();
    }

    /**
     * 为角色分配权限
     */
    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('role:assign')")
    public ResponseDTO<Void> assignPermissionsToRole(@PathVariable Long id, @RequestBody Long[] permissionIds) {
        roleService.assignPermissionsToRole(id, permissionIds);
        return ResponseDTO.success();
    }
}