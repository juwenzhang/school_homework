> 学校实训作业仓库，代码很垃圾，没什么可以看的

```text
# 跳过测试打包构建
mvn clean package -DskipTests

# 清理之前的构建缓存
mvn clean

# 重新构建 common 模块（带上调试日志，方便排查）
mvn compile -pl :common -X

# 重新构建 blog-service 模块： 指定模块进行编译
mvn compile -rf :blog-service

# 安装 common 模块到本地仓库
mvn install -pl common -am

# 启动 blog-service 模块
mvn spring-boot:run -pl :blog-service

# 编译整个服务
mvn compile

# 编译详情信息
mvn compile -pl server_name -am

# 运行整个服务
mvn spring-boot:run
```