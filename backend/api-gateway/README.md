# API网关模块 (api-gateway)

## 概述
api-gateway是博客系统的API网关模块，负责请求路由、负载均衡、安全认证、限流熔断等功能，是系统的统一入口。

## 目录结构
```
api-gateway/
└── src/main/java/com/blog/apigateway/
    ├── config/            # 配置类
    ├── filter/            # 过滤器
    └── ApiGatewayApplication.java # 应用入口
```

## 主要功能

### 1. 请求路由
- 根据请求路径将请求转发到对应的服务
- 支持路径重写和路径映射

### 2. 负载均衡
- 自动整合Ribbon实现负载均衡
- 支持多种负载均衡策略

### 3. 安全认证
- JWT令牌验证
- 请求签名验证
- IP白名单/黑名单

### 4. 限流熔断
- 请求限流（基于QPS或并发数）
- 服务熔断和降级
- 超时重试机制

### 5. 请求日志
- 记录请求和响应日志
- 支持请求跟踪

### 6. 跨域处理
- 配置跨域资源共享（CORS）

## 技术栈
- Spring Boot 3.2.0
- Spring Cloud 2021.0.8
- Spring Cloud Alibaba 2021.0.5.0
- Spring Cloud Gateway
- Nacos
- Spring Security

## API文档

### 路由配置
API网关模块本身不提供业务API，而是负责路由请求到其他服务。以下是各服务的路由前缀：

| 服务名称 | 路由前缀 | 服务端口 |
|---------|---------|---------|
| user-service | /api/users/ | 8081 |
| blog-service | /api/articles/, /api/categories/, /api/tags/, /api/comments/, /api/tracking/ | 8082 |
| monitor-service | /api/monitor/ | 8083 |
| admin-service | /api/admin/ | 8084 |

### 全局过滤器

#### JWT认证过滤器
```
路径: /** (除了公开API)
功能: 验证请求中的JWT令牌
请求头: Authorization: Bearer {token}
响应: 401 Unauthorized (令牌无效或过期)
```

#### 请求日志过滤器
```
路径: /**
功能: 记录请求和响应的详细信息
```

### 限流配置
```
全局默认限流: 100 QPS
针对特定路径可配置不同的限流规则
```

## 依赖关系
- common模块：提供共享实体类、工具类等
- Spring Cloud组件：服务注册发现、配置中心等

## 使用说明
1. 确保Nacos服务已启动
2. 确保其他微服务已注册到Nacos
3. 运行ApiGatewayApplication类启动API网关
4. 通过API网关访问其他微服务的接口

## 配置文件
主要配置位于application.yml文件，包括：
- 服务端口和名称
- Nacos配置
- 路由配置
- 过滤器配置
- 限流配置
- 跨域配置

## 路由配置示例
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1
            - JwtAuth=exclude=/api/auth/login,/api/auth/register
            - RequestRateLimiter=100,100
        
        - id: blog-service
          uri: lb://blog-service
          predicates:
            - Path=/api/articles/**,/api/categories/**,/api/tags/**,/api/comments/**,/api/tracking/**
          filters:
            - StripPrefix=1
            - JwtAuth=exclude=/api/articles/**,/api/categories/**,/api/tags/**,/api/comments/**
            - RequestRateLimiter=200,200

        - id: monitor-service
          uri: lb://monitor-service
          predicates:
            - Path=/api/monitor/**
          filters:
            - StripPrefix=1
            - JwtAuth=exclude=none
            - RequestRateLimiter=50,50

        - id: admin-service
          uri: lb://admin-service
          predicates:
            - Path=/api/admin/**
          filters:
            - StripPrefix=1
            - JwtAuth=exclude=/api/admin/auth/login
            - RequestRateLimiter=50,50
```

## 跨域配置
```yaml
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
            allowCredentials: true
```