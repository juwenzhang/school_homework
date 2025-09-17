package com.blog.userservice.service.impl;

import com.blog.common.entity.Permission;
import com.blog.common.entity.Role;
import com.blog.common.entity.User;
import com.blog.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * UserDetailsService实现类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new UsernameNotFoundException("用户已禁用: " + username);
        }

        // 检查用户是否已删除
        if (user.getDeleted()) {
            throw new UsernameNotFoundException("用户已删除: " + username);
        }

        // 获取用户权限列表
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 添加角色权限
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

                // 添加权限权限
                Set<Permission> permissions = role.getPermissionSet();
                for (Permission permission : permissions) {
                    authorities.add(new SimpleGrantedAuthority(permission.getCode()));
                }
            }

        // 创建并返回UserDetails对象
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}