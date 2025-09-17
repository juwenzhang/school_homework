# 用户服务模块 (user-service)

## 概述
user-service是博客系统的用户管理服务模块，负责用户的注册、登录、权限管理、个人信息维护等功能。

## 目录结构
```
user-service/
└── src/main/java/com/blog/userservice/
    ├── config/            # 配置类
    ├── controller/        # 控制器
    ├── entity/            # 实体类
    ├── exception/         # 异常处理
    ├── mapper/            # 映射器
    ├── repository/        # 数据访问层
    ├── security/          # 安全相关
    └── service/           # 服务层
```

## 主要功能

### 1. 用户管理
- 用户注册
- 用户登录/注销
- 用户信息查询与更新
- 用户状态管理

### 2. 角色管理
- 角色创建与查询
- 角色修改与删除
- 角色分配权限

### 3. 权限管理
- 权限创建与查询
- 权限修改与删除
- 权限分配给角色

### 4. 安全认证与授权
- JWT令牌生成与验证
- 基于角色的访问控制
- 密码加密存储

## 技术栈
- Spring Boot 3.2.0
- Spring Cloud 2021.0.8
- Spring Cloud Alibaba 2021.0.5.0
- Spring Security
- Spring Data JPA
- PostgreSQL
- Redis
- Nacos

## API文档

### 用户相关API

#### 注册用户
```
POST /api/auth/register
请求体: {
  "username": "string",
  "password": "string",
  "email": "string",
  "nickname": "string"
}
响应: {
  "code": "200",
  "message": "注册成功",
  "data": {
    "id": "long",
    "username": "string",
    "nickname": "string",
    "email": "string",
    "createTime": "date-time"
  }
}
```

#### 用户登录
```
POST /api/auth/login
请求体: {
  "username": "string",
  "password": "string"
}
响应: {
  "code": "200",
  "message": "登录成功",
  "data": {
    "token": "string",
    "tokenType": "Bearer",
    "expiresIn": "long",
    "user": {
      "id": "long",
      "username": "string",
      "nickname": "string",
      "email": "string",
      "roles": ["string"]
    }
  }
}
```

#### 获取当前用户信息
```
GET /api/users/me
请求头: Authorization: Bearer {token}
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "id": "long",
    "username": "string",
    "nickname": "string",
    "email": "string",
    "avatar": "string",
    "bio": "string",
    "status": "integer",
    "createTime": "date-time",
    "updateTime": "date-time",
    "roles": ["string"]
  }
}
```

## 依赖关系
- common模块：提供共享实体类、工具类等
- Spring Cloud组件：服务注册发现、配置中心等

## 使用说明
1. 确保Nacos服务已启动
2. 确保PostgreSQL和Redis服务已配置并启动
3. 运行UserServiceApplication类启动用户服务
4. 通过API网关访问用户服务的API接口

## 配置文件
主要配置位于application.yml文件，包括：
- 服务端口和名称
- 数据库连接信息
- Redis连接信息
- Nacos配置
- 安全配置