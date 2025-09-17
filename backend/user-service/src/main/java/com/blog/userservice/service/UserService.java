package com.blog.userservice.service;

import com.blog.common.dto.PageQueryDTO;
import com.blog.common.dto.PageResultDTO;
import com.blog.common.dto.UserDTO;
import com.blog.common.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据用户ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    UserDTO getUserById(Long id);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserDTO getUserByUsername(String username);

    /**
     * 根据邮箱获取用户信息
     * @param email 邮箱
     * @return 用户信息
     */
    UserDTO getUserByEmail(String email);

    /**
     * 分页查询用户列表
     * @param pageQueryDTO 分页查询参数
     * @return 用户列表
     */
    PageResultDTO<UserDTO> listUsers(PageQueryDTO pageQueryDTO);

    /**
     * 创建用户
     * @param userDTO 用户信息
     * @return 创建后的用户信息
     */
    UserDTO createUser(UserDTO userDTO);

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param userDTO 用户信息
     * @return 更新后的用户信息
     */
    UserDTO updateUser(Long id, UserDTO userDTO);

    /**
     * 删除用户
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 修改用户状态
     * @param id 用户ID
     * @param status 用户状态
     */
    void changeUserStatus(Long id, Integer status);

    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void assignRolesToUser(Long userId, Long[] roleIds);

    /**
     * 重置用户密码
     * @param id 用户ID
     * @param newPassword 新密码
     */
    void resetPassword(Long id, String newPassword);

    /**
     * 用户修改密码
     * @param id 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long id, String oldPassword, String newPassword);

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @return 注册后的用户信息
     */
    UserDTO register(String username, String password, String email);
    
    /**
     * 用户注册（通过DTO）
     * @param userDTO 用户DTO
     * @return 注册后的用户信息
     */
    UserDTO registerUser(UserDTO userDTO);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户信息和JWT令牌
     */
    UserDTO login(String username, String password);
    
    /**
     * 更新用户登录信息
     * @param userId 用户ID
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     */
    void updateLoginInfo(Long userId, String ipAddress, String userAgent);

    /**
     * 将用户实体转换为DTO
     * @param user 用户实体
     * @return 用户DTO
     */
    UserDTO convertToDTO(User user);

    /**
     * 将用户DTO转换为实体
     * @param userDTO 用户DTO
     * @return 用户实体
     */
    User convertToEntity(UserDTO userDTO);
}