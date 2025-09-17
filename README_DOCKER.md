# Docker部署指南

## 项目概述
本博客系统采用微服务架构，包含多个后端服务模块。本指南将介绍如何使用Docker快速部署和运行整个系统。

## 前提条件
- 安装 [Docker](https://docs.docker.com/get-docker/)
- 安装 [Docker Compose](https://docs.docker.com/compose/install/)
- 安装 [Git](https://git-scm.com/downloads)
- 安装 [JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- 安装 [Maven 3.8+](https://maven.apache.org/download.cgi)

## 系统架构

### 基础设施服务
- **PostgreSQL**: 数据库服务
- **Redis**: 缓存服务
- **Nacos**: 服务注册与配置中心

### 后端微服务
- **api-gateway**: API网关服务 (端口: 8080)
- **user-service**: 用户服务 (端口: 8081)
- **blog-service**: 博客服务 (端口: 8082)
- **monitor-service**: 监控服务 (端口: 8083)
- **admin-service**: 管理员服务 (端口: 8084)
- **common**: 公共模块 (库模块)

## 快速开始

### 1. 克隆项目
```bash
# 克隆仓库
git clone https://github.com/your-username/blog-homework.git
cd blog-homework
```

### 2. 使用构建脚本
项目根目录提供了 `build-docker.sh` 脚本，用于简化Docker镜像构建和服务启动流程。

```bash
# 给脚本添加执行权限
chmod +x build-docker.sh

# 构建所有服务的Docker镜像
./build-docker.sh build

# 启动所有服务
./build-docker.sh start

# 构建镜像并启动服务 (一步完成)
./build-docker.sh all

# 停止所有服务
./build-docker.sh stop

# 查看帮助信息
./build-docker.sh help
```

### 3. 手动使用Docker Compose
如果不使用构建脚本，也可以手动执行以下命令：

```bash
# 构建所有服务的Docker镜像
cd backend
mvn clean package -DskipTests
cd ..

# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f

# 停止所有服务
docker-compose down
```

## 服务访问
启动成功后，可以通过以下地址访问各服务：

- **API网关**: http://localhost:8080
- **Nacos控制台**: http://localhost:8848/nacos (默认用户名/密码: nacos/nacos)
- **PostgreSQL**: localhost:5432 (用户名/密码: blog_user/blog_password, 数据库: blog_db)
- **Redis**: localhost:6379

## 环境变量配置
主要环境变量配置位于 `docker-compose.yml` 文件中，包括：

- 数据库连接信息
- Nacos服务地址
- Redis服务地址
- JVM参数配置
- 服务端口配置

## 自定义配置

### 1. 修改配置文件
各服务的Docker环境配置文件位于 `backend/{service-name}/src/main/resources/application-docker.yml`，可以根据需要进行修改。

### 2. 修改Dockerfile
各服务的Dockerfile位于 `backend/{service-name}/Dockerfile`，可以根据需要调整基础镜像、JVM参数等。

### 3. 修改docker-compose.yml
可以根据需要调整服务配置、端口映射、环境变量等。

## 开发流程

### 本地开发
在本地开发环境中，可以只启动基础设施服务（PostgreSQL、Redis、Nacos），然后在IDE中启动各个微服务：

```bash
# 只启动基础设施服务
docker-compose up -d postgres redis nacos
```

### 构建单个服务
可以使用以下命令单独构建某个服务的Docker镜像：

```bash
# 进入服务目录
cd backend/user-service

# 构建Docker镜像
docker build -t blog-user-service:latest .

# 运行单个服务容器
docker run -d --name blog-user-service --network blog-homework_blog-network -p 8081:8081 blog-user-service:latest
```

## 常见问题

### 1. 服务启动失败
- 查看服务日志：`docker-compose logs {service-name}`
- 检查基础设施服务是否正常运行：`docker-compose ps postgres redis nacos`
- 检查服务依赖配置是否正确

### 2. 数据库连接失败
- 检查PostgreSQL服务是否启动：`docker-compose ps postgres`
- 检查数据库连接参数是否正确
- 查看数据库日志：`docker-compose logs postgres`

### 3. 服务注册失败
- 检查Nacos服务是否启动：`docker-compose ps nacos`
- 检查服务的Nacos配置是否正确
- 查看Nacos控制台，确认服务是否成功注册

### 4. 端口冲突
- 如果本地端口已被占用，可以修改`docker-compose.yml`中的端口映射配置

## 性能优化建议

1. **调整JVM参数**：根据服务器资源情况，调整`Dockerfile`中的`JAVA_OPTS`参数
2. **使用健康检查**：已经在`docker-compose.yml`中配置了健康检查，可以根据需要调整检查间隔和超时时间
3. **设置资源限制**：可以在`docker-compose.yml`中为每个服务添加资源限制配置，例如：
   ```yaml
   deploy:
     resources:
       limits:
         cpus: "1.0"
         memory: "1024M"
   ```
4. **使用Docker镜像缓存**：构建镜像时利用Docker的缓存机制，加快构建速度

## 安全建议

1. **修改默认密码**：在生产环境中，务必修改数据库、Redis、Nacos等服务的默认密码
2. **配置HTTPS**：在生产环境中，为API网关配置HTTPS
3. **设置JWT密钥**：在生产环境中，设置强JWT密钥
4. **限制容器网络**：使用Docker网络隔离，限制服务之间的访问权限
5. **定期更新镜像**：定期更新基础镜像和依赖，修复安全漏洞

## 持续集成/持续部署
可以将构建脚本集成到CI/CD流程中，实现自动化构建、测试和部署。