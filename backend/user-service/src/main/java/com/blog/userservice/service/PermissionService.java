package com.blog.userservice.service;

import com.blog.common.dto.PageQueryDTO;
import com.blog.common.dto.PageResultDTO;
import com.blog.common.dto.PermissionDTO;
import com.blog.common.entity.Permission;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService {

    /**
     * 根据权限ID获取权限信息
     * @param id 权限ID
     * @return 权限信息
     */
    PermissionDTO getPermissionById(Long id);

    /**
     * 根据权限名称获取权限信息
     * @param name 权限名称
     * @return 权限信息
     */
    PermissionDTO getPermissionByName(String name);

    /**
     * 根据权限编码获取权限信息
     * @param code 权限编码
     * @return 权限信息
     */
    PermissionDTO getPermissionByCode(String code);

    /**
     * 分页查询权限列表
     * @param pageQueryDTO 分页查询参数
     * @return 权限列表
     */
    PageResultDTO<PermissionDTO> listPermissions(PageQueryDTO pageQueryDTO);

    /**
     * 获取所有权限列表
     * @return 权限列表
     */
    List<PermissionDTO> getAllPermissions();

    /**
     * 根据权限类型查询权限列表
     * @param type 权限类型
     * @return 权限列表
     */
    List<PermissionDTO> getPermissionsByType(Integer type);

    /**
     * 根据父权限ID查询子权限列表
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<PermissionDTO> getPermissionsByParentId(Long parentId);

    /**
     * 创建权限
     * @param permissionDTO 权限信息
     * @return 创建后的权限信息
     */
    PermissionDTO createPermission(PermissionDTO permissionDTO);

    /**
     * 更新权限信息
     * @param id 权限ID
     * @param permissionDTO 权限信息
     * @return 更新后的权限信息
     */
    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO);

    /**
     * 删除权限
     * @param id 权限ID
     */
    void deletePermission(Long id);

    /**
     * 批量删除权限
     * @param ids 权限ID列表
     */
    void deletePermissions(Long[] ids);

    /**
     * 修改权限状态
     * @param id 权限ID
     * @param status 状态（1启用，0禁用）
     */
    void changePermissionStatus(Long id, Integer status);

    /**
     * 将权限实体转换为DTO
     * @param permission 权限实体
     * @return 权限DTO
     */
    PermissionDTO convertToDTO(Permission permission);

    /**
     * 将权限DTO转换为实体
     * @param permissionDTO 权限DTO
     * @return 权限实体
     */
    Permission convertToEntity(PermissionDTO permissionDTO);

    /**
     * 构建权限树
     * @param permissions 权限列表
     * @return 权限树
     */
    List<PermissionDTO> buildPermissionTree(List<PermissionDTO> permissions);
}