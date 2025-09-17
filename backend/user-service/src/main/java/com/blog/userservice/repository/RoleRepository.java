package com.blog.userservice.repository;

import com.blog.common.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色仓库接口
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 根据角色名称查询角色
     * @param roleName 角色名称
     * @return 角色信息
     */
    Optional<Role> findByRoleName(String roleName);

    /**
     * 检查角色名称是否已存在
     * @param roleName 角色名称
     * @return 是否存在
     */
    Boolean existsByRoleName(String roleName);
}