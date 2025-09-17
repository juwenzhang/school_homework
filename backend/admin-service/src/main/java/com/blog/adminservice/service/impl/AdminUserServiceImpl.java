package com.blog.adminservice.service.impl;

import com.blog.adminservice.entity.AdminUser;
import com.blog.adminservice.repository.AdminUserRepository;
import com.blog.adminservice.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserServiceImpl(AdminUserRepository adminUserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AdminUser createAdminUser(AdminUser adminUser) {
        // 检查用户名和邮箱是否已存在
        if (existsByUsername(adminUser.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (existsByEmail(adminUser.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 加密密码
        adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        adminUser.setCreateTime(LocalDateTime.now());
        adminUser.setUpdateTime(LocalDateTime.now());
        
        return adminUserRepository.save(adminUser);
    }

    @Override
    @Transactional
    public AdminUser updateAdminUser(Long id, AdminUser adminUser) {
        AdminUser existingAdminUser = adminUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("管理员用户不存在"));
        
        // 更新用户信息，但不更新密码
        existingAdminUser.setNickname(adminUser.getNickname());
        existingAdminUser.setEmail(adminUser.getEmail());
        existingAdminUser.setAvatar(adminUser.getAvatar());
        existingAdminUser.setEnabled(adminUser.getEnabled());
        existingAdminUser.setUpdateTime(LocalDateTime.now());
        
        return adminUserRepository.save(existingAdminUser);
    }

    @Override
    public Page<AdminUser> findAllAdminUsers(Pageable pageable) {
        return adminUserRepository.findAll(pageable);
    }

    @Override
    public Optional<AdminUser> findAdminUserById(Long id) {
        return adminUserRepository.findById(id);
    }

    @Override
    public Optional<AdminUser> findAdminUserByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public AdminUser enableOrDisableAdminUser(Long id, boolean enabled) {
        AdminUser adminUser = adminUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("管理员用户不存在"));
        
        adminUser.setEnabled(enabled);
        adminUser.setUpdateTime(LocalDateTime.now());
        
        return adminUserRepository.save(adminUser);
    }

    @Override
    @Transactional
    public AdminUser resetAdminUserPassword(Long id, String newPassword) {
        AdminUser adminUser = adminUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("管理员用户不存在"));
        
        // 加密新密码
        adminUser.setPassword(passwordEncoder.encode(newPassword));
        adminUser.setUpdateTime(LocalDateTime.now());
        
        return adminUserRepository.save(adminUser);
    }

    @Override
    @Transactional
    public void deleteAdminUser(Long id) {
        if (!adminUserRepository.existsById(id)) {
            throw new RuntimeException("管理员用户不存在");
        }
        adminUserRepository.deleteById(id);
    }

    @Override
    public long countAdminUsers() {
        return adminUserRepository.countAllAdminUsers();
    }

    @Override
    public List<AdminUser> searchAdminUsers(String keyword) {
        Iterable<AdminUser> iterable = adminUserRepository.searchByUsernameOrEmail(keyword);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsername(String username) {
        return adminUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return adminUserRepository.existsByEmail(email);
    }
}