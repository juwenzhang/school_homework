package com.blog.userservice.service.impl;

import com.blog.common.dto.PageQueryDTO;
import com.blog.common.dto.PageResultDTO;
import com.blog.common.dto.PermissionDTO;
import com.blog.common.entity.Permission;
import com.blog.common.util.CacheUtils;
import com.blog.userservice.exception.PermissionException;
import com.blog.userservice.repository.PermissionRepository;
import com.blog.userservice.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private CacheUtils cacheUtils;

    @Override
    public PermissionDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionException("权限不存在"));
        return convertToDTO(permission);
    }

    @Override
    public PermissionDTO getPermissionByName(String name) {
        Permission permission = permissionRepository.findByPermissionName(name)
                .orElseThrow(() -> new PermissionException("权限不存在"));
        return convertToDTO(permission);
    }

    @Override
    public PermissionDTO getPermissionByCode(String code) {
        Permission permission = permissionRepository.findByPermissionCode(code)
                .orElseThrow(() -> new PermissionException("权限不存在"));
        return convertToDTO(permission);
    }

    @Override
    public PageResultDTO<PermissionDTO> listPermissions(PageQueryDTO pageQueryDTO) {
        pageQueryDTO.validateAndFix();
        Pageable pageable = PageRequest.of(
                pageQueryDTO.getPageNum() - 1,
                pageQueryDTO.getPageSize(),
                Sort.by(Sort.Direction.ASC, "sort")
        );
        Page<Permission> permissionPage = permissionRepository.findAll(pageable);
        List<PermissionDTO> permissionDTOList = permissionPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return PageResultDTO.build(
                permissionPage.getTotalElements(),
                pageQueryDTO.getPageNum(),
                pageQueryDTO.getPageSize(),
                permissionDTOList
        );
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        List<Permission> permissionList = permissionRepository.findAll(
                Sort.by(Sort.Direction.ASC, "sort")
        );
        return permissionList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionDTO> getPermissionsByType(Integer type) {
        List<Permission> permissionList = permissionRepository.findByType(type);
        return permissionList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionDTO> getPermissionsByParentId(Long parentId) {
        List<Permission> permissionList = permissionRepository.findByParentId(parentId);
        return permissionList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        // 检查权限名称是否已存在
        if (permissionRepository.existsByPermissionName(permissionDTO.getPermissionName())) {
            throw new PermissionException("权限名称已存在");
        }
        // 检查权限编码是否已存在
        if (permissionRepository.existsByPermissionCode(permissionDTO.getPermissionCode())) {
            throw new PermissionException("权限编码已存在");
        }
        Permission permission = convertToEntity(permissionDTO);
        // 设置创建时间和更新时间
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        Permission savedPermission = permissionRepository.save(permission);
        return convertToDTO(savedPermission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionException("权限不存在"));
        // 检查权限名称是否重复
        if (permissionRepository.existsByPermissionName(permissionDTO.getPermissionName())) {
            Permission existingPermission = permissionRepository.findByPermissionName(permissionDTO.getPermissionName()).get();
            if (!existingPermission.getId().equals(id)) {
                throw new PermissionException("权限名称已存在");
            }
        }
        // 检查权限编码是否重复
        if (permissionRepository.existsByPermissionCode(permissionDTO.getPermissionCode())) {
            Permission existingPermission = permissionRepository.findByPermissionCode(permissionDTO.getPermissionCode()).get();
            if (!existingPermission.getId().equals(id)) {
                throw new PermissionException("权限编码已存在");
            }
        }
        BeanUtils.copyProperties(permissionDTO, permission, "id", "createTime");
        // 更新时间
        permission.setUpdateTime(LocalDateTime.now());
        Permission updatedPermission = permissionRepository.save(permission);
        return convertToDTO(updatedPermission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionException("权限不存在"));
        permissionRepository.delete(permission);
        // 清除缓存
        cacheUtils.delete(CacheUtils.buildPermissionCacheKey(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermissions(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        for (Long id : ids) {
            Permission permission = permissionRepository.findById(id)
                    .orElseThrow(() -> new PermissionException("权限不存在: " + id));
            permissionRepository.delete(permission);
            // 清除缓存
            cacheUtils.delete(CacheUtils.buildPermissionCacheKey(id));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePermissionStatus(Long id, Integer status) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionException("权限不存在"));
        permission.setStatus(status);
        permission.setUpdateTime(LocalDateTime.now());
        permissionRepository.save(permission);
        // 清除缓存
        cacheUtils.delete(CacheUtils.buildPermissionCacheKey(id));
    }

    @Override
    public PermissionDTO convertToDTO(Permission permission) {
        if (permission == null) {
            return null;
        }
        PermissionDTO permissionDTO = new PermissionDTO();
        BeanUtils.copyProperties(permission, permissionDTO);
        // 处理子权限信息
        if (permission.getChildren() != null && !permission.getChildren().isEmpty()) {
            List<PermissionDTO> childrenDTOs = permission.getChildren().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            permissionDTO.setChildren(childrenDTOs);
        }
        return permissionDTO;
    }

    @Override
    public Permission convertToEntity(PermissionDTO permissionDTO) {
        if (permissionDTO == null) {
            return null;
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        // 子权限信息在创建或更新时单独处理
        return permission;
    }

    @Override
    public List<PermissionDTO> buildPermissionTree(List<PermissionDTO> permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            return new ArrayList<>();
        }
        
        // 构建权限树结构
        List<PermissionDTO> roots = new ArrayList<>();
        Map<Long, PermissionDTO> permissionMap = new HashMap<>();
        
        // 先将所有权限放入Map中
        for (PermissionDTO permission : permissions) {
            permissionMap.put(permission.getId(), permission);
        }
        
        // 构建树
        for (PermissionDTO permission : permissions) {
            Long parentId = permission.getParentId();
            if (parentId == null || parentId == 0) {
                // 根节点
                roots.add(permission);
            } else {
                // 非根节点，添加到父节点的子节点列表中
                PermissionDTO parent = permissionMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(permission);
                }
            }
        }
        
        // 对子节点进行排序
        for (PermissionDTO root : roots) {
            sortChildren(root);
        }
        
        return roots;
    }
    
    /**
     * 递归排序子节点
     */
    private void sortChildren(PermissionDTO permission) {
        if (permission.getChildren() != null && !permission.getChildren().isEmpty()) {
            permission.getChildren().sort(Comparator.comparing(PermissionDTO::getSort));
            for (PermissionDTO child : permission.getChildren()) {
                sortChildren(child);
            }
        }
    }
}