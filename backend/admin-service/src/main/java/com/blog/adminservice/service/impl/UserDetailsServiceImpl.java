package com.blog.adminservice.service.impl;

import com.blog.adminservice.entity.AdminUser;
import com.blog.adminservice.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    @Autowired
    public UserDetailsServiceImpl(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查找管理员用户
        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户名或密码错误"));

        // 验证用户状态
        if (!adminUser.getEnabled()) {
            throw new UsernameNotFoundException("用户已被禁用");
        }
        if (!adminUser.getAccountNonExpired()) {
            throw new UsernameNotFoundException("用户账户已过期");
        }
        if (!adminUser.getAccountNonLocked()) {
            throw new UsernameNotFoundException("用户账户已被锁定");
        }
        if (!adminUser.getCredentialsNonExpired()) {
            throw new UsernameNotFoundException("用户凭证已过期");
        }

        // 创建权限列表
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_ADMIN"));

        // 创建UserDetails对象
        return new User(
                adminUser.getUsername(),
                adminUser.getPassword(),
                adminUser.getEnabled(),
                adminUser.getAccountNonExpired(),
                adminUser.getCredentialsNonExpired(),
                adminUser.getAccountNonLocked(),
                authorities
        );
    }
}