package com.blog.userservice.service;

import com.blog.common.dto.UserDTO;
import com.blog.userservice.UserServiceApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserService单元测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserServiceApplication.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    /**
     * 测试创建用户功能
     */
    @Test
    public void testCreateUser() {
        // 创建用户DTO
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser" + System.currentTimeMillis());
        userDTO.setNickname("测试用户");
        userDTO.setEmail("test" + System.currentTimeMillis() + "@example.com");

        // 调用创建用户方法
        UserDTO createdUser = userService.createUser(userDTO);

        // 验证结果
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(userDTO.getUsername(), createdUser.getUsername());
        assertEquals(userDTO.getNickname(), createdUser.getNickname());
        assertEquals(userDTO.getEmail(), createdUser.getEmail());
    }

    /**
     * 测试根据ID获取用户功能
     */
    @Test
    public void testGetUserById() {
        // 先创建一个用户用于测试
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser" + System.currentTimeMillis());
        userDTO.setNickname("测试用户");
        UserDTO createdUser = userService.createUser(userDTO);

        // 根据ID获取用户
        UserDTO foundUser = userService.getUserById(createdUser.getId());

        // 验证结果
        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals(createdUser.getUsername(), foundUser.getUsername());
    }

    /**
     * 测试更新用户功能
     */
    @Test
    public void testUpdateUser() {
        // 先创建一个用户用于测试
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser" + System.currentTimeMillis());
        userDTO.setNickname("测试用户");
        UserDTO createdUser = userService.createUser(userDTO);

        // 修改用户信息
        UserDTO updateDTO = new UserDTO();
        updateDTO.setNickname("更新后的测试用户");
        updateDTO.setBio("这是测试用户的简介");

        // 调用更新用户方法
        UserDTO updatedUser = userService.updateUser(createdUser.getId(), updateDTO);

        // 验证结果
        assertNotNull(updatedUser);
        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals("更新后的测试用户", updatedUser.getNickname());
        assertEquals("这是测试用户的简介", updatedUser.getBio());
    }

    /**
     * 测试删除用户功能
     */
    @Test
    public void testDeleteUser() {
        // 先创建一个用户用于测试
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser" + System.currentTimeMillis());
        userDTO.setNickname("测试用户");
        UserDTO createdUser = userService.createUser(userDTO);

        // 调用删除用户方法
        userService.deleteUser(createdUser.getId());

        // 验证用户已被删除
        assertThrows(Exception.class, () -> {
            userService.getUserById(createdUser.getId());
        });
    }

    /**
     * 测试修改用户状态功能
     */
    @Test
    public void testChangeUserStatus() {
        // 先创建一个用户用于测试
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser" + System.currentTimeMillis());
        userDTO.setNickname("测试用户");
        UserDTO createdUser = userService.createUser(userDTO);

        // 修改用户状态为禁用
        userService.changeUserStatus(createdUser.getId(), 0);

        // 验证用户状态已修改
        UserDTO updatedUser = userService.getUserById(createdUser.getId());
        assertEquals(0, updatedUser.getStatus());

        // 修改用户状态为启用
        userService.changeUserStatus(createdUser.getId(), 1);
        updatedUser = userService.getUserById(createdUser.getId());
        assertEquals(1, updatedUser.getStatus());
    }
}