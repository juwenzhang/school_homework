package com.blog.userservice.service.impl;

import com.blog.common.constant.SystemConstants;
import com.blog.common.dto.PageQueryDTO;
import com.blog.common.dto.PageResultDTO;
import com.blog.common.dto.UserDTO;
import com.blog.common.entity.Role;
import com.blog.common.entity.User;
import com.blog.common.exception.UserException;
import com.blog.common.util.CacheUtils;
import com.blog.common.util.JwtUtils;
import com.blog.userservice.repository.RoleRepository;
import com.blog.userservice.repository.UserRepository;
import com.blog.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CacheUtils cacheUtils;

    @Override
    public UserDTO getUserById(Long id) {
        // 先从缓存中获取
        String cacheKey = CacheUtils.buildUserCacheKey(id);
        UserDTO userDTO = cacheUtils.get(cacheKey);
        if (userDTO != null) {
            return userDTO;
        }

        // 缓存中不存在则从数据库查询
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("用户不存在"));
        userDTO = convertToDTO(user);

        // 存入缓存
        cacheUtils.setWithDefaultExpire(cacheKey, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("用户不存在"));
        return convertToDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("用户不存在"));
        return convertToDTO(user);
    }

    @Override
    public PageResultDTO<UserDTO> listUsers(PageQueryDTO pageQueryDTO) {
        pageQueryDTO.validateAndFix();

        Pageable pageable = PageRequest.of(
                pageQueryDTO.getPageNum() - 1,
                pageQueryDTO.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDTO> userDTOList = userPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResultDTO.build(
                userPage.getTotalElements(),
                pageQueryDTO.getPageNum(),
                pageQueryDTO.getPageSize(),
                userDTOList
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO createUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UserException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (StringUtils.isNotBlank(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserException("邮箱已被使用");
        }

        User user = convertToEntity(userDTO);
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 设置默认头像
        if (StringUtils.isBlank(user.getAvatar())) {
            user.setAvatar(SystemConstants.USER_DEFAULT_AVATAR);
        }
        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(SystemConstants.USER_STATUS_ENABLED);
        }

        // 保存用户
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("用户不存在"));

        // 检查用户名是否被其他用户使用
        if (StringUtils.isNotBlank(userDTO.getUsername()) && !user.getUsername().equals(userDTO.getUsername())) {
            if (userRepository.existsByUsername(userDTO.getUsername())) {
                throw new UserException("用户名已存在");
            }
            user.setUsername(userDTO.getUsername());
        }

        // 检查邮箱是否被其他用户使用
        if (StringUtils.isNotBlank(userDTO.getEmail()) && !user.getEmail().equals(userDTO.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new UserException("邮箱已被使用");
            }
            user.setEmail(userDTO.getEmail());
        }

        // 更新其他字段
        if (StringUtils.isNotBlank(userDTO.getNickname())) {
            user.setNickname(userDTO.getNickname());
        }
        if (StringUtils.isNotBlank(userDTO.getAvatar())) {
            user.setAvatar(userDTO.getAvatar());
        }
        if (userDTO.getStatus() != null) {
            user.setStatus(userDTO.getStatus());
        }

        User updatedUser = userRepository.save(user);

        // 清除缓存
        String cacheKey = CacheUtils.buildUserCacheKey(id);
        cacheUtils.delete(cacheKey);

        return convertToDTO(updatedUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("用户不存在"));
        userRepository.delete(user);

        // 清除缓存
        String cacheKey = CacheUtils.buildUserCacheKey(id);
        cacheUtils.delete(cacheKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUserStatus(Long id, Integer status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("用户不存在"));
        user.setStatus(status);
        userRepository.save(user);

        // 清除缓存
        String cacheKey = CacheUtils.buildUserCacheKey(id);
        cacheUtils.delete(cacheKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRolesToUser(Long userId, Long[] roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("用户不存在"));

        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new UserException("角色不存在: " + roleId));
            roles.add(role);
        }

        user.setRoles(roles);
        userRepository.save(user);

        // 清除缓存
        String cacheKey = CacheUtils.buildUserCacheKey(userId);
        cacheUtils.delete(cacheKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("用户不存在"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 清除缓存
        String cacheKey = CacheUtils.buildUserCacheKey(id);
        cacheUtils.delete(cacheKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("用户不存在"));

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UserException("旧密码错误");
        }

        // 设置新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 清除缓存
        String cacheKey = CacheUtils.buildUserCacheKey(id);
        cacheUtils.delete(cacheKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO register(String username, String password, String email) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new UserException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (StringUtils.isNotBlank(email) && userRepository.existsByEmail(email)) {
            throw new UserException("邮箱已被使用");
        }

        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setStatus(SystemConstants.USER_STATUS_ENABLED);
        user.setAvatar(SystemConstants.USER_DEFAULT_AVATAR);

        // 分配默认角色
        Role userRole = roleRepository.findByRoleName(SystemConstants.USER_ROLE_USER)
                .orElseThrow(() -> new UserException("默认角色不存在"));
        user.setRoles(Collections.singleton(userRole));

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO registerUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UserException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (StringUtils.isNotBlank(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserException("邮箱已被使用");
        }

        // 创建用户
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setNickname(userDTO.getNickname());
        user.setStatus(SystemConstants.USER_STATUS_ENABLED);
        user.setAvatar(StringUtils.isNotBlank(userDTO.getAvatar()) ? userDTO.getAvatar() : SystemConstants.USER_DEFAULT_AVATAR);

        // 分配默认角色
        Role userRole = roleRepository.findByRoleName(SystemConstants.USER_ROLE_USER)
                .orElseThrow(() -> new UserException("默认角色不存在"));
        user.setRoles(Collections.singleton(userRole));

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("用户名或密码错误"));

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != SystemConstants.USER_STATUS_ENABLED) {
            throw new UserException("用户已被禁用");
        }

        UserDTO userDTO = convertToDTO(user);
        // 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        String token = JwtUtils.generateToken(user.getId(), user.getUsername(), claims);

        // 将令牌设置到DTO中返回
        // 注意：实际项目中不建议直接返回密码等敏感信息
        // 这里仅做示例，实际应该返回更简洁的用户信息和令牌
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", userDTO);
        
        // 由于接口定义返回UserDTO，这里我们需要做一些调整
        // 实际项目中应该考虑修改接口定义以返回包含token的响应对象
        // 但为了保持与当前接口定义的兼容性，我们只返回用户信息
        return userDTO;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginInfo(Long userId, String ipAddress, String userAgent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("用户不存在"));
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        
        // 增加登录计数
        user.setLoginCount(user.getLoginCount() + 1);
        
        // 设置IP地址和User Agent
        if (ipAddress != null) {
            user.setIpAddress(ipAddress);
        }
        if (userAgent != null) {
            user.setUserAgent(userAgent);
        }
        
        userRepository.save(user);
        
        // 清除缓存
        String cacheKey = CacheUtils.buildUserCacheKey(userId);
        cacheUtils.delete(cacheKey);
    }

    @Override
    public UserDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);

        // 处理角色信息
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<RoleDTO> roleDTOs = new ArrayList<>();
            for (Role role : user.getRoles()) {
                RoleDTO roleDTO = new RoleDTO();
                BeanUtils.copyProperties(role, roleDTO);
                roleDTOs.add(roleDTO);
            }
            userDTO.setRoles(roleDTOs);
        }

        return userDTO;
    }

    @Override
    public User convertToEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        // 处理角色信息
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (RoleDTO roleDTO : userDTO.getRoles()) {
                Role role = roleRepository.findById(roleDTO.getId())
                        .orElseThrow(() -> new RoleException("角色不存在: " + roleDTO.getId()));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        return user;
    }
}