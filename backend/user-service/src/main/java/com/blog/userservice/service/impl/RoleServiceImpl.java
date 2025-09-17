package com.blog.userservice.service.impl;

import com.blog.common.dto.PageQueryDTO;
import com.blog.common.dto.PageResultDTO;
import com.blog.common.dto.RoleDTO;
import com.blog.common.dto.UserDTO;
import com.blog.common.entity.Permission;
import com.blog.common.entity.Role;
import com.blog.common.entity.User;
import com.blog.common.exception.RoleException;
import com.blog.userservice.repository.PermissionRepository;
import com.blog.userservice.repository.RoleRepository;
import com.blog.userservice.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleException("角色不存在"));
        return convertToDTO(role);
    }

    @Override
    public RoleDTO getRoleByName(String name) {
        Role role = roleRepository.findByRoleName(name)
                .orElseThrow(() -> new RoleException("角色不存在"));
        return convertToDTO(role);
    }

    @Override
    public PageResultDTO<RoleDTO> listRoles(PageQueryDTO pageQueryDTO) {
        pageQueryDTO.validateAndFix();
        Pageable pageable = PageRequest.of(
                pageQueryDTO.getPageNum() - 1,
                pageQueryDTO.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );
        Page<Role> rolePage = roleRepository.findAll(pageable);
        List<RoleDTO> roleDTOList = rolePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return PageResultDTO.build(
                rolePage.getTotalElements(),
                pageQueryDTO.getPageNum(),
                pageQueryDTO.getPageSize(),
                roleDTOList
        );
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roleList = roleRepository.findAll();
        return roleList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO createRole(RoleDTO roleDTO) {
        // 检查角色名称是否已存在
        if (roleRepository.existsByRoleName(roleDTO.getName())) {
            throw new RoleException("角色名称已存在");
        }
        Role role = convertToEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleException("角色不存在"));
        // 检查角色名称是否重复
        if (roleRepository.findByRoleName(roleDTO.getName()).isPresent() && 
                !role.getId().equals(roleRepository.findByRoleName(roleDTO.getName()).get().getId())) {
            throw new RoleException("角色名称已存在");
        }
        BeanUtils.copyProperties(roleDTO, role, "id", "createdTime");
        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleException("角色不存在"));
        roleRepository.delete(role);
    }

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissionsToRole(Long roleId, Long[] permissionIds) {
        // 根据角色ID查询角色
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleException("角色不存在"));
        
        // 查询所有权限
        Set<Permission> permissions = new HashSet<>();
        if (permissionIds != null && permissionIds.length > 0) {
            for (Long permissionId : permissionIds) {
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new RoleException("权限不存在: " + permissionId));
                permissions.add(permission);
            }
        }
        
        // 设置角色的权限
        role.setPermissions(permissions);
        
        // 保存角色信息
        roleRepository.save(role);
        
        // 清除相关缓存
        cacheUtils.delete(CacheUtils.buildRoleCacheKey(roleId));
    }

    @Override
    public RoleDTO convertToDTO(Role role) {
        if (role == null) {
            return null;
        }
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(role, roleDTO);
        return roleDTO;
    }

    @Override
    public Role convertToEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        return role;
    }
}