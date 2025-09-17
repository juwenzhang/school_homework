# 管理员服务模块 (admin-service)

## 概述
admin-service是博客系统的管理员服务模块，负责管理员用户的管理、系统配置管理、日志审计等功能。

## 目录结构
```
admin-service/
└── src/main/java/com/blog/adminservice/
    ├── config/            # 配置类
    ├── controller/        # 控制器
    ├── entity/            # 实体类
    ├── exception/         # 异常处理
    ├── repository/        # 数据访问层
    ├── service/           # 服务层
    └── AdminServiceApplication.java # 应用入口
```

## 主要功能

### 1. 管理员用户管理
- 管理员用户的增删改查
- 管理员角色和权限管理
- 密码重置和修改

### 2. 系统配置管理
- 系统全局配置项管理
- 邮件配置管理
- 第三方服务配置管理

### 3. 日志审计
- 管理员操作日志记录
- 系统异常日志记录
- 日志查询和导出

### 4. 用户管理
- 普通用户列表查询
- 用户状态管理
- 用户角色分配

### 5. 数据统计
- 用户增长统计
- 内容发布统计
- 系统访问统计

## 技术栈
- Spring Boot 3.2.0
- Spring Cloud 2021.0.8
- Spring Cloud Alibaba 2021.0.5.0
- Spring Data JPA
- PostgreSQL
- Nacos
- Spring Security
- Spring Cache
- Redis

## API文档

### 管理员用户相关API

#### 管理员登录
```
POST /api/admin/auth/login
请求体: {
  "username": "string",
  "password": "string"
}
响应: {
  "code": "200",
  "message": "登录成功",
  "data": {
    "token": "string",
    "tokenType": "string",
    "expiresIn": "integer",
    "userInfo": {
      "id": "long",
      "username": "string",
      "roles": ["string"]
    }
  }
}
```

#### 管理员退出登录
```
POST /api/admin/auth/logout
请求头: Authorization: Bearer {token}
响应: {
  "code": "200",
  "message": "退出成功"
}
```

#### 获取管理员列表
```
GET /api/admin/users
请求头: Authorization: Bearer {token}
参数: page=integer, size=integer, username=string
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "content": [{
      "id": "long",
      "username": "string",
      "email": "string",
      "status": "string",
      "createTime": "date-time",
      "lastLoginTime": "date-time",
      "roles": ["string"]
    }],
    "totalElements": "integer",
    "totalPages": "integer",
    "number": "integer",
    "size": "integer"
  }
}
```

#### 创建管理员
```
POST /api/admin/users
请求头: Authorization: Bearer {token}
请求体: {
  "username": "string",
  "password": "string",
  "email": "string",
  "status": "string",
  "roles": ["string"]
}
响应: {
  "code": "200",
  "message": "创建成功",
  "data": {
    "id": "long",
    "username": "string",
    "email": "string",
    "status": "string",
    "createTime": "date-time",
    "roles": ["string"]
  }
}
```

#### 更新管理员信息
```
PUT /api/admin/users/{id}
请求头: Authorization: Bearer {token}
请求体: {
  "username": "string",
  "email": "string",
  "status": "string",
  "roles": ["string"]
}
响应: {
  "code": "200",
  "message": "更新成功",
  "data": {
    "id": "long",
    "username": "string",
    "email": "string",
    "status": "string",
    "createTime": "date-time",
    "lastLoginTime": "date-time",
    "roles": ["string"]
  }
}
```

#### 重置管理员密码
```
POST /api/admin/users/{id}/reset-password
请求头: Authorization: Bearer {token}
请求体: {
  "newPassword": "string"
}
响应: {
  "code": "200",
  "message": "密码重置成功"
}
```

### 系统配置相关API

#### 获取系统配置
```
GET /api/admin/config
请求头: Authorization: Bearer {token}
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "siteName": "string",
    "siteDescription": "string",
    "logoUrl": "string",
    "contactEmail": "string",
    "smtpServer": "string",
    "smtpPort": "integer",
    "smtpUsername": "string",
    "smtpPassword": "string",
    "maxFileSize": "long",
    "defaultPageSize": "integer"
  }
}
```

#### 更新系统配置
```
PUT /api/admin/config
请求头: Authorization: Bearer {token}
请求体: {
  "siteName": "string",
  "siteDescription": "string",
  "logoUrl": "string",
  "contactEmail": "string",
  "smtpServer": "string",
  "smtpPort": "integer",
  "smtpUsername": "string",
  "smtpPassword": "string",
  "maxFileSize": "long",
  "defaultPageSize": "integer"
}
响应: {
  "code": "200",
  "message": "更新成功"
}
```

### 操作日志相关API

#### 获取操作日志
```
GET /api/admin/logs/operation
请求头: Authorization: Bearer {token}
参数: page=integer, size=integer, username=string, operation=string, startTime=date-time, endTime=date-time
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "content": [{
      "id": "long",
      "username": "string",
      "operation": "string",
      "description": "string",
      "ip": "string",
      "userAgent": "string",
      "createTime": "date-time"
    }],
    "totalElements": "integer",
    "totalPages": "integer",
    "number": "integer",
    "size": "integer"
  }
}
```

### 数据统计相关API

#### 获取系统统计数据
```
GET /api/admin/stats
请求头: Authorization: Bearer {token}
参数: startDate=date-time, endDate=date-time
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "userCount": "integer",
    "articleCount": "integer",
    "commentCount": "integer",
    "viewCount": "integer",
    "dailyActiveUsers": [{
      "date": "date",
      "count": "integer"
    }],
    "dailyArticleViews": [{
      "date": "date",
      "count": "integer"
    }]
  }
}
```

## 依赖关系
- common模块：提供共享实体类、工具类等
- Spring Cloud组件：服务注册发现、配置中心等
- user-service：用户相关操作
- blog-service：内容管理相关操作
- monitor-service：系统监控相关操作

## 使用说明
1. 确保Nacos服务已启动
2. 确保PostgreSQL服务已配置并启动
3. 确保Redis服务已配置并启动
4. 运行AdminServiceApplication类启动管理员服务
5. 通过API网关访问管理员服务的API接口

## 配置文件
主要配置位于application.yml文件，包括：
- 服务端口和名称
- 数据库连接信息
- Redis配置
- Nacos配置
- 安全认证配置
- 缓存配置

## 管理员角色权限
系统预定义以下管理员角色：

| 角色名称 | 角色描述 | 主要权限 |
|---------|---------|---------|
| SUPER_ADMIN | 超级管理员 | 系统所有权限 |
| ADMIN | 普通管理员 | 内容管理、用户管理等普通权限 |
| AUDITOR | 审计员 | 查看日志、查看统计数据等只读权限 |