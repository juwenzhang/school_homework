# 使用官方的 OpenJDK 基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制构建好的jar包到容器中
COPY target/blog-service-1.0.0.jar blog-service.jar

# 设置环境变量
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"

# 暴露端口
EXPOSE 8082

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar blog-service.jar"]