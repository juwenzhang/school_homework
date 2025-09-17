package com.blog.userservice.service;

import com.blog.common.dto.PageQueryDTO;
import com.blog.common.dto.PageResultDTO;
import com.blog.common.dto.RoleDTO;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {

    /**
     * 根据角色ID获取角色信息
     * @param id 角色ID
     * @return 角色信息
     */
    RoleDTO getRoleById(Long id);

    /**
     * 根据角色名称获取角色信息
     * @param name 角色名称
     * @return 角色信息
     */
    RoleDTO getRoleByName(String name);

    /**
     * 分页查询角色列表
     * @param pageQueryDTO 分页查询参数
     * @return 角色列表
     */
    PageResultDTO<RoleDTO> listRoles(PageQueryDTO pageQueryDTO);

    /**
     * 获取所有角色列表
     * @return 角色列表
     */
    List<RoleDTO> getAllRoles();

    /**
     * 创建角色
     * @param roleDTO 角色信息
     * @return 创建后的角色信息
     */
    RoleDTO createRole(RoleDTO roleDTO);

    /**
     * 更新角色信息
     * @param id 角色ID
     * @param roleDTO 角色信息
     * @return 更新后的角色信息
     */
    RoleDTO updateRole(Long id, RoleDTO roleDTO);

    /**
     * 删除角色
     * @param id 角色ID
     */
    void deleteRole(Long id);

    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void assignPermissionsToRole(Long roleId, Long[] permissionIds);

    /**
     * 将角色实体转换为DTO
     * @param role 角色实体
     * @return 角色DTO
     */
    RoleDTO convertToDTO(com.blog.common.entity.Role role);

    /**
     * 将角色DTO转换为实体
     * @param roleDTO 角色DTO
     * @return 角色实体
     */
    com.blog.common.entity.Role convertToEntity(RoleDTO roleDTO);
}