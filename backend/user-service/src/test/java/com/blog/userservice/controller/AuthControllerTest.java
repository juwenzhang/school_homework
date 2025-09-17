package com.blog.userservice.controller;

import com.blog.userservice.UserServiceApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * AuthController单元测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserServiceApplication.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试用户注册功能
     */
    @Test
    public void testRegister() throws Exception {
        String username = "testregister" + System.currentTimeMillis();
        String email = username + "@example.com";
        String password = "test123456";

        // 构建请求体
        String requestBody = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"nickname\":\"测试注册用户\"," +
                "\"email\":\"" + email + "\"" +
                "}";

        // 发送请求并验证结果
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(email));
    }

    /**
     * 测试用户登录功能
     */
    @Test
    public void testLogin() throws Exception {
        // 先注册一个用户
        String username = "testlogin" + System.currentTimeMillis();
        String email = username + "@example.com";
        String password = "test123456";

        // 注册用户
        String registerBody = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"nickname\":\"测试登录用户\"," +
                "\"email\":\"" + email + "\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));

        // 登录测试
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .param("username", username)
                        .param("password", password))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.user.username").value(username));
    }

    /**
     * 测试用户注销功能
     */
    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    /**
     * 测试获取当前登录用户信息
     */
    @Test
    public void testGetCurrentUser() throws Exception {
        // 先注册并登录获取token
        String username = "testcurrentuser" + System.currentTimeMillis();
        String password = "test123456";

        // 注册用户
        String registerBody = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"nickname\":\"测试用户\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // 获取token
        String responseContent = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 提取token（简化处理，实际项目中可以使用JSON解析器）
        int tokenStartIndex = responseContent.indexOf("\"token\":\"") + 8;
        int tokenEndIndex = responseContent.indexOf("\"", tokenStartIndex);
        String token = responseContent.substring(tokenStartIndex, tokenEndIndex);

        // 使用token访问需要认证的接口
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/current")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(username));
    }
}