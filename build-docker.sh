#!/bin/bash

# 设置脚本执行目录为脚本所在目录
cd "$(dirname "$0")"

# 确保脚本可执行
chmod +x "$0"

# 设置颜色变量
green='\033[0;32m'
red='\033[0;31m'
yellow='\033[0;33m'
reset='\033[0m'

# 函数：打印带颜色的消息
print_message() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${reset}"
}

# 函数：构建单个模块
build_module() {
    local module_name=$1
    local module_path=$2
    local module_port=$3
    
    print_message "${yellow}" "开始构建 ${module_name} 模块..."
    
    # 进入模块目录
    cd "$module_path"
    
    # 执行Maven构建
    mvn clean package -DskipTests
    
    # 检查Maven构建是否成功
    if [ $? -ne 0 ]; then
        print_message "${red}" "${module_name} 模块构建失败！"
        cd - > /dev/null
        return 1
    fi
    
    # 构建Docker镜像
    docker build -t "blog-${module_name,,}:latest" .
    
    # 检查Docker构建是否成功
    if [ $? -ne 0 ]; then
        print_message "${red}" "${module_name} 模块Docker镜像构建失败！"
        cd - > /dev/null
        return 1
    fi
    
    print_message "${green}" "${module_name} 模块构建成功！"
    cd - > /dev/null
    return 0
}

# 函数：构建所有模块
build_all() {
    # 构建common模块
    build_module "common" "backend/common" ""
    if [ $? -ne 0 ]; then
        return 1
    fi
    
    # 构建user-service模块
    build_module "user-service" "backend/user-service" "8081"
    if [ $? -ne 0 ]; then
        return 1
    fi
    
    # 构建blog-service模块
    build_module "blog-service" "backend/blog-service" "8082"
    if [ $? -ne 0 ]; then
        return 1
    fi
    
    # 构建monitor-service模块
    build_module "monitor-service" "backend/monitor-service" "8083"
    if [ $? -ne 0 ]; then
        return 1
    fi
    
    # 构建admin-service模块
    build_module "admin-service" "backend/admin-service" "8084"
    if [ $? -ne 0 ]; then
        return 1
    fi
    
    # 构建api-gateway模块
    build_module "api-gateway" "backend/api-gateway" "8080"
    if [ $? -ne 0 ]; then
        return 1
    fi
    
    return 0
}

# 函数：启动Docker Compose
start_docker_compose() {
    print_message "${yellow}" "启动Docker Compose服务..."
    
    # 检查docker-compose命令是否存在
    if ! command -v docker-compose &> /dev/null; then
        print_message "${red}" "未找到docker-compose命令，请先安装！"
        return 1
    fi
    
    # 启动Docker Compose
    docker-compose up -d
    
    # 检查Docker Compose是否成功启动
    if [ $? -ne 0 ]; then
        print_message "${red}" "Docker Compose启动失败！"
        return 1
    fi
    
    print_message "${green}" "Docker Compose服务启动成功！"
    print_message "${green}" "服务访问地址："
    print_message "${green}" "- API Gateway: http://localhost:8080"
    print_message "${green}" "- Nacos Console: http://localhost:8848/nacos"
    print_message "${green}" "使用 'docker-compose logs -f' 查看服务日志"
    return 0
}

# 函数：停止Docker Compose
stop_docker_compose() {
    print_message "${yellow}" "停止Docker Compose服务..."
    
    # 停止Docker Compose
    docker-compose down
    
    # 检查Docker Compose是否成功停止
    if [ $? -ne 0 ]; then
        print_message "${red}" "Docker Compose停止失败！"
        return 1
    fi
    
    print_message "${green}" "Docker Compose服务停止成功！"
    return 0
}

# 函数：显示帮助信息
show_help() {
    echo "博客系统Docker构建脚本"
    echo "用法: ./build-docker.sh [选项]"
    echo ""
    echo "选项:"
    echo "  build         构建所有模块的Docker镜像"
    echo "  start         启动Docker Compose服务"
    echo "  stop          停止Docker Compose服务"
    echo "  all           构建所有模块并启动Docker Compose服务"
    echo "  help          显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  ./build-docker.sh build   # 只构建Docker镜像"
    echo "  ./build-docker.sh start   # 只启动Docker Compose服务"
    echo "  ./build-docker.sh all     # 构建镜像并启动服务"
}

# 主函数
main() {
    # 检查是否有参数
    if [ $# -eq 0 ]; then
        show_help
        return 1
    fi
    
    # 根据参数执行相应的函数
    case "$1" in
        build)
            build_all
            ;;
        start)
            start_docker_compose
            ;;
        stop)
            stop_docker_compose
            ;;
        all)
            build_all
            if [ $? -eq 0 ]; then
                start_docker_compose
            fi
            ;;
        help)
            show_help
            ;;
        *)
            print_message "${red}" "无效的选项: $1"
            show_help
            return 1
            ;;
    esac
    
    return 0
}

# 执行主函数
main "$@"