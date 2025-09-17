package com.blog.blogservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService实现类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // @Autowired
    // private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // User user = userService.findByUsername(username);
        // if (user == null) {
        //     throw new UsernameNotFoundException("用户不存在: " + username);
        // }
        
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6") // 密码为：password
                .roles("USER")
                .build();
    }
}