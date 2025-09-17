# 监控服务模块 (monitor-service)

## 概述
monitor-service是博客系统的监控服务模块，负责收集系统指标、服务指标和JVM指标，进行告警检测并通过多种渠道发送通知。

## 目录结构
```
monitor-service/
└── src/main/java/com/blog/monitorservice/
    ├── alert/             # 告警相关
    ├── collector/         # 指标收集相关
    ├── config/            # 配置类
    ├── controller/        # 控制器
    ├── exception/         # 异常处理
    └── notification/      # 通知相关
```

## 主要功能

### 1. 指标收集
- 系统指标收集（CPU使用率、内存使用率、磁盘空间等）
- 服务指标收集（响应时间、请求成功率、并发连接数等）
- JVM指标收集（堆内存使用情况、GC次数、线程数等）

### 2. 告警检测
- 根据配置的阈值检测指标异常
- 告警状态管理（告警触发、告警恢复）
- 告警等级划分（INFO、WARNING、ERROR、CRITICAL）

### 3. 通知服务
- 支持多种通知渠道（邮件、Webhook等）
- 通知策略配置（通知频率、通知条件等）
- 通知内容格式化

### 4. 监控数据查询
- 历史指标数据查询
- 告警记录查询
- 系统状态概览

## 技术栈
- Spring Boot 3.2.0
- Spring Cloud 2021.0.8
- Spring Cloud Alibaba 2021.0.5.0
- Spring Data JPA
- PostgreSQL
- Nacos
- Spring Boot Actuator

## API文档

### 指标相关API

#### 获取系统指标
```
GET /api/monitor/system-metrics
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "cpuUsage": "double",
    "memoryUsage": "double",
    "diskSpace": "double",
    "networkIn": "long",
    "networkOut": "long",
    "timestamp": "date-time"
  }
}
```

#### 获取服务指标
```
GET /api/monitor/service-metrics
响应: {
  "code": "200",
  "message": "查询成功",
  "data": [{
    "serviceName": "string",
    "responseTime": "double",
    "successRate": "double",
    "concurrentConnections": "integer",
    "errorCount": "integer",
    "timestamp": "date-time"
  }]
}
```

#### 获取JVM指标
```
GET /api/monitor/jvm-metrics
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "heapMemoryUsage": "double",
    "nonHeapMemoryUsage": "double",
    "threadCount": "integer",
    "gcCount": "integer",
    "gcTime": "long",
    "timestamp": "date-time"
  }
}
```

### 告警相关API

#### 获取当前告警列表
```
GET /api/monitor/alerts
响应: {
  "code": "200",
  "message": "查询成功",
  "data": [{
    "id": "long",
    "alertType": "string",
    "severity": "string",
    "message": "string",
    "status": "string",
    "triggerTime": "date-time",
    "recoveryTime": "date-time",
    "serviceName": "string"
  }]
}
```

#### 获取告警配置
```
GET /api/monitor/alert-config
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "cpuThreshold": "double",
    "memoryThreshold": "double",
    "diskThreshold": "double",
    "responseTimeThreshold": "double",
    "errorRateThreshold": "double",
    "notificationConfig": {
      "emailEnabled": "boolean",
      "webhookEnabled": "boolean",
      "notifyOn": ["string"],
      "retryCount": "integer",
      "retryInterval": "integer"
    }
  }
}
```

## 依赖关系
- common模块：提供共享实体类、工具类等
- Spring Cloud组件：服务注册发现、配置中心等

## 使用说明
1. 确保Nacos服务已启动
2. 确保PostgreSQL服务已配置并启动
3. 运行MonitorServiceApplication类启动监控服务
4. 通过API网关访问监控服务的API接口

## 配置文件
主要配置位于application.yml文件，包括：
- 服务端口和名称
- 数据库连接信息
- Nacos配置
- 指标收集配置
- 告警阈值配置
- 通知配置（邮件、Webhook等）

## 通知渠道配置

### 邮件通知配置
```yaml
monitor.notification.email.enabled: true
monitor.notification.email.host: smtp.example.com
monitor.notification.email.port: 587
monitor.notification.email.username: username
monitor.notification.email.password: password
monitor.notification.email.from: from@example.com
monitor.notification.email.to: to@example.com
```

### Webhook通知配置
```yaml
monitor.notification.webhook.enabled: true
monitor.notification.webhook.url: https://webhook.example.com
monitor.notification.webhook.secret: secret_key
```