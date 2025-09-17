# 博客服务模块 (blog-service)

博客系统的核心服务模块，负责文章、分类、标签、评论等内容的管理。

## 技术栈

- **框架**：Spring Boot 3.2.0
- **ORM**：Spring Data JPA
- **数据库**：PostgreSQL
- **缓存**：Redis
- **服务注册与发现**：Nacos
- **安全认证**：Spring Security + JWT
- **API文档**：SpringDoc OpenAPI (Swagger)
- **构建工具**：Maven
- **语言**：Java 17

## 项目结构

```
src/main/java/com/blog/blogservice/
├── config/            # 配置类
├── controller/        # 控制器
├── entity/            # 实体类
├── exception/         # 异常处理
├── repository/        # 数据访问层
├── service/           # 业务逻辑层
│   └── impl/          # 业务逻辑实现
├── utils/             # 工具类
└── BlogServiceApplication.java # 应用入口
```

## 主要功能

1. **文章管理**
   - 创建、更新、删除文章
   - 文章查询（分页、按条件筛选）
   - 文章发布/下架
   - 文章阅读量统计
   - 文章点赞

2. **分类管理**
   - 创建、更新、删除分类
   - 分类查询
   - 启用/禁用分类

3. **标签管理**
   - 创建、更新、删除标签
   - 标签查询

4. **评论管理**
   - 创建、更新、删除评论
   - 评论查询（分页、按文章查询）
   - 评论审核
   - 评论点赞

5. **埋点统计**
   - 用户行为跟踪
   - 页面访问统计
   - 热门文章统计
   - 用户活跃度统计

6. **安全认证**
   - JWT令牌认证
   - 权限控制

## 环境要求

- JDK 17或更高版本
- Maven 3.8或更高版本
- PostgreSQL 14或更高版本
- Redis 6或更高版本
- Nacos 2.2或更高版本

## 配置说明

主要配置文件位于 `src/main/resources/application.yml`，包含以下主要配置：

- 服务器配置（端口、上下文路径）
- 数据库连接配置
- JPA配置
- Redis配置
- Nacos服务发现配置
- JWT配置
- 日志配置
- 缓存配置
- 分页配置
- 文件上传配置
- 限流配置

## 启动说明

1. 确保已安装并启动PostgreSQL、Redis和Nacos服务
2. 配置 `application.yml` 中的相关参数
3. 使用Maven构建项目：`mvn clean package`
4. 启动应用：`java -jar target/blog-service-1.0.0.jar`

## API文档

### 文章相关API

#### 创建文章
```
POST /api/articles
请求头: Authorization: Bearer {token}
请求体: {
  "title": "string",
  "content": "string",
  "summary": "string",
  "coverImage": "string",
  "status": "integer",
  "categoryId": "long",
  "tagIds": ["long"]
}
响应: {
  "code": "200",
  "message": "创建成功",
  "data": {
    "id": "long",
    "title": "string",
    "summary": "string",
    "coverImage": "string",
    "status": "integer",
    "viewCount": "long",
    "commentCount": "long",
    "createTime": "date-time",
    "updateTime": "date-time",
    "category": {
      "id": "long",
      "name": "string"
    },
    "tags": [{
      "id": "long",
      "name": "string"
    }]
  }
}
```

#### 获取文章列表
```
GET /api/articles?page={page}&size={size}&categoryId={categoryId}&tagId={tagId}&keyword={keyword}
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "content": [{
      "id": "long",
      "title": "string",
      "summary": "string",
      "coverImage": "string",
      "viewCount": "long",
      "commentCount": "long",
      "createTime": "date-time",
      "category": {
        "id": "long",
        "name": "string"
      },
      "tags": [{
        "id": "long",
        "name": "string"
      }]
    }],
    "totalElements": "long",
    "totalPages": "integer",
    "pageNumber": "integer",
    "pageSize": "integer"
  }
}
```

#### 获取文章详情
```
GET /api/articles/{id}
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "id": "long",
    "title": "string",
    "content": "string",
    "summary": "string",
    "coverImage": "string",
    "viewCount": "long",
    "commentCount": "long",
    "createTime": "date-time",
    "updateTime": "date-time",
    "category": {
      "id": "long",
      "name": "string"
    },
    "tags": [{
      "id": "long",
      "name": "string"
    }]
  }
}
```

#### 更新文章
```
PUT /api/articles/{id}
请求头: Authorization: Bearer {token}
请求体: {
  "title": "string",
  "content": "string",
  "summary": "string",
  "coverImage": "string",
  "status": "integer",
  "categoryId": "long",
  "tagIds": ["long"]
}
响应: {
  "code": "200",
  "message": "更新成功",
  "data": {
    "id": "long",
    "title": "string",
    "summary": "string",
    "coverImage": "string",
    "status": "integer",
    "viewCount": "long",
    "commentCount": "long",
    "createTime": "date-time",
    "updateTime": "date-time",
    "category": {
      "id": "long",
      "name": "string"
    },
    "tags": [{
      "id": "long",
      "name": "string"
    }]
  }
}
```

#### 删除文章
```
DELETE /api/articles/{id}
请求头: Authorization: Bearer {token}
响应: {
  "code": "200",
  "message": "删除成功"
}
```

### 分类相关API

#### 创建分类
```
POST /api/categories
请求头: Authorization: Bearer {token}
请求体: {
  "name": "string",
  "description": "string",
  "parentId": "long",
  "icon": "string",
  "sort": "integer"
}
响应: {
  "code": "200",
  "message": "创建成功",
  "data": {
    "id": "long",
    "name": "string",
    "description": "string",
    "parentId": "long",
    "icon": "string",
    "sort": "integer",
    "articleCount": "integer",
    "createTime": "date-time",
    "updateTime": "date-time"
  }
}
```

#### 获取分类列表
```
GET /api/categories
响应: {
  "code": "200",
  "message": "查询成功",
  "data": [{
    "id": "long",
    "name": "string",
    "description": "string",
    "parentId": "long",
    "icon": "string",
    "sort": "integer",
    "articleCount": "integer",
    "createTime": "date-time",
    "updateTime": "date-time"
  }]
}
```

#### 获取分类详情
```
GET /api/categories/{id}
响应: {
  "code": "200",
  "message": "查询成功",
  "data": {
    "id": "long",
    "name": "string",
    "description": "string",
    "parentId": "long",
    "icon": "string",
    "sort": "integer",
    "articleCount": "integer",
    "createTime": "date-time",
    "updateTime": "date-time",
    "children": [{
      "id": "long",
      "name": "string",
      "description": "string",
      "parentId": "long",
      "icon": "string",
      "sort": "integer",
      "articleCount": "integer",
      "createTime": "date-time",
      "updateTime": "date-time"
    }]
  }
}
```

### 标签相关API

#### 创建标签
```
POST /api/tags
请求头: Authorization: Bearer {token}
请求体: {
  "name": "string"
}
响应: {
  "code": "200",
  "message": "创建成功",
  "data": {
    "id": "long",
    "name": "string",
    "articleCount": "integer",
    "createTime": "date-time",
    "updateTime": "date-time"
  }
}
```

#### 获取标签列表
```
GET /api/tags
响应: {
  "code": "200",
  "message": "查询成功",
  "data": [{
    "id": "long",
    "name": "string",
    "articleCount": "integer",
    "createTime": "date-time",
    "updateTime": "date-time"
  }]
}
```

### 评论相关API

#### 添加评论
```
POST /api/comments
请求体: {
  "articleId": "long",
  "content": "string",
  "parentId": "long" // 可选，回复评论时提供
}
响应: {
  "code": "200",
  "message": "评论成功",
  "data": {
    "id": "long",
    "content": "string",
    "createTime": "date-time",
    "user": {
      "id": "long",
      "username": "string",
      "avatar": "string"
    },
    "parentComment": {
      "id": "long",
      "content": "string",
      "user": {
        "id": "long",
        "username": "string"
      }
    }
  }
}
```

#### 获取文章评论
```
GET /api/articles/{articleId}/comments
响应: {
  "code": "200",
  "message": "查询成功",
  "data": [{
    "id": "long",
    "content": "string",
    "createTime": "date-time",
    "user": {
      "id": "long",
      "username": "string",
      "avatar": "string"
    },
    "replies": [{
      "id": "long",
      "content": "string",
      "createTime": "date-time",
      "user": {
        "id": "long",
        "username": "string",
        "avatar": "string"
      },
      "parentComment": {
        "id": "long",
        "content": "string",
        "user": {
          "id": "long",
          "username": "string"
        }
      }
    }]
  }]
}
```

#### 删除评论
```
DELETE /api/comments/{id}
请求头: Authorization: Bearer {token}
响应: {
  "code": "200",
  "message": "删除成功"
}
```

### 统计相关API

#### 记录文章浏览
```
POST /api/tracking/article-views
请求体: {
  "articleId": "long"
}
响应: {
  "code": "200",
  "message": "记录成功"
}
```

应用启动后，可以通过以下地址访问API文档：

```- Swagger UI: http://localhost:8082/api/swagger-ui.html
- API JSON: http://localhost:8082/api/v3/api-docs

## 开发指南

1. **实体类开发**
   - 在 `entity` 包下创建实体类，使用JPA注解映射数据库表
   - 确保实体类之间的关系正确定义（一对一、一对多、多对多等）

2. **数据访问层开发**
   - 在 `repository` 包下创建Repository接口，继承自JpaRepository
   - 可以定义自定义查询方法，使用Spring Data JPA的方法命名规则或@Query注解

3. **业务逻辑层开发**
   - 在 `service` 包下创建Service接口，定义业务方法
   - 在 `service/impl` 包下创建ServiceImpl类，实现Service接口
   - 使用@Service和@Transactional注解标记服务类

4. **控制器开发**
   - 在 `controller` 包下创建Controller类，使用@RestController注解
   - 定义API端点，使用@RequestMapping、@GetMapping、@PostMapping等注解
   - 处理请求参数和返回响应结果

5. **异常处理**
   - 使用 `BlogException` 自定义业务异常
   - 在 `GlobalExceptionHandler` 中添加异常处理方法

6. **安全认证**
   - 使用JWT令牌进行认证
   - 配置 `SecurityConfig` 中的安全规则

## 测试

- 单元测试：使用JUnit 5和Spring Boot Test进行单元测试
- 集成测试：测试服务之间的交互和API功能

## 部署

- 可以使用Docker容器化部署
- 支持Kubernetes集群部署（配置文件位于项目根目录的deployment文件夹）
