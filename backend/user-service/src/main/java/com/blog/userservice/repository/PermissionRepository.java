package com.blog.userservice.repository;

import com.blog.common.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限仓库接口
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * 根据权限名称查询权限
     * @param permissionName 权限名称
     * @return 权限信息
     */
    Optional<Permission> findByPermissionName(String permissionName);

    /**
     * 根据权限编码查询权限
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    Optional<Permission> findByPermissionCode(String permissionCode);

    /**
     * 根据权限类型查询权限列表
     * @param type 权限类型
     * @return 权限列表
     */
    List<Permission> findByType(Integer type);

    /**
     * 根据父权限ID查询子权限列表
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<Permission> findByParentId(Long parentId);

    /**
     * 检查权限名称是否已存在
     * @param permissionName 权限名称
     * @return 是否存在
     */
    Boolean existsByPermissionName(String permissionName);

    /**
     * 检查权限编码是否已存在
     * @param permissionCode 权限编码
     * @return 是否存在
     */
    Boolean existsByPermissionCode(String permissionCode);
}