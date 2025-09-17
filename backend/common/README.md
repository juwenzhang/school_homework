# 公共模块 (common)

## 概述
common模块是博客系统的公共基础模块，包含了系统中所有服务模块共享的实体类、工具类、常量定义、异常处理和配置等。

## 目录结构
```
common/
└── src/main/java/com/blog/common/
    ├── config/            # 公共配置类
    ├── constant/          # 常量定义
    ├── dto/               # 数据传输对象
    ├── entity/            # 实体类
    ├── exception/         # 异常处理
    └── util/              # 工具类
```

## 主要功能

### 1. 实体类 (Entity)
包含系统中所有共享的实体类，如User、Role、Permission、Article、Category、Tag等。

### 2. 工具类 (Util)
提供常用的工具方法，如：
- CacheUtils: 缓存相关工具方法
- JwtUtils: JWT令牌生成和验证工具方法

### 3. 常量定义 (Constant)
定义系统中使用的常量，如：
- SystemConstants: 系统级常量定义

### 4. 异常处理 (Exception)
提供统一的异常处理类，如：
- BlogException: 基础异常类
- GlobalExceptionHandler: 全局异常处理器
- ValidationErrorDetails: 验证错误详情类

### 5. 数据传输对象 (DTO)
提供服务间数据传输的对象定义。

## 依赖关系
common模块是其他所有模块的基础依赖，其他模块通过以下方式依赖该模块：
```xml
<dependency>
    <groupId>com.blog</groupId>
    <artifactId>common</artifactId>
    <version>${project.version}</version>
</dependency>
```

## 使用说明
1. 在其他模块的pom.xml文件中添加对common模块的依赖
2. 直接导入并使用common模块中提供的类和方法
3. 对于需要扩展的功能，可以在各自的模块中进行扩展，而不是修改common模块

## 注意事项
1. common模块中的类和方法应该是高度通用的，不应该包含特定于某个业务模块的逻辑
2. 修改common模块中的代码需要谨慎，因为这会影响到所有依赖该模块的服务
3. 新增功能时，应考虑是否真的需要放在common模块中，还是应该放在特定的业务模块中