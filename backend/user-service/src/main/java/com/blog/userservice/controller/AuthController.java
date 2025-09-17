package com.blog.userservice.controller;

import com.blog.common.dto.ResponseDTO;
import com.blog.common.dto.UserDTO;
import com.blog.common.util.JwtUtils;
import com.blog.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseDTO<Map<String, Object>> login(@RequestParam String username, @RequestParam String password,
                                                 HttpServletRequest request) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 设置安全上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 获取用户信息
        UserDTO userDTO = userService.getUserByUsername(username);
        
        // 生成JWT令牌
        String jwt = jwtUtils.generateToken(userDTO.getId(), username);

        // 获取IP地址和User Agent
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        // 更新登录信息
        userService.updateLoginInfo(userDTO.getId(), ipAddress, userAgent);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);
        result.put("user", userDTO);

        return ResponseDTO.success(result);
    }
    
    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，取第一个IP地址
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseDTO<UserDTO> register(@RequestBody UserDTO userDTO) {
        UserDTO registeredUser = userService.registerUser(userDTO);
        return ResponseDTO.success(registeredUser);
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public ResponseDTO<Void> logout() {
        // 在前后端分离项目中，注销通常是客户端删除令牌
        // 这里可以添加一些额外的清理操作，如清除用户会话信息等
        SecurityContextHolder.clearContext();
        return ResponseDTO.success();
    }

    /**
     * 刷新JWT令牌
     */
    @PostMapping("/refresh")
    public ResponseDTO<Map<String, String>> refreshToken(@RequestParam String token) {
        if (jwtUtils.validateToken(token)) {
            String username = jwtUtils.getUsernameFromToken(token);
            // 从令牌中获取用户ID
            Long userId = jwtUtils.getUserIdFromToken(token);
            String newToken = jwtUtils.generateToken(userId, username);
            
            Map<String, String> result = new HashMap<>();
            result.put("token", newToken);
            
            return ResponseDTO.success(result);
        }
        return ResponseDTO.fail(401, "令牌无效或已过期");
    }

    /**
     * 验证令牌
     */
    @GetMapping("/validate")
    public ResponseDTO<Map<String, Object>> validateToken(@RequestParam String token) {
        boolean isValid = jwtUtils.validateToken(token);
        Map<String, Object> result = new HashMap<>();
        result.put("valid", isValid);
        
        if (isValid) {
            String username = jwtUtils.getUsernameFromToken(token);
            result.put("username", username);
            result.put("expiration", jwtUtils.getExpirationDateFromToken(token));
        }
        
        return ResponseDTO.success(result);
    }
}