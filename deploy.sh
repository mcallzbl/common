#!/bin/bash

# Common Spring Library 发布脚本
# 作者: mcallzbl
# 用途: 发布common-spring库到不同的Maven仓库

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}===================================${NC}"
    echo -e "${BLUE} $1 ${NC}"
    echo -e "${BLUE}===================================${NC}"
}

# 检查是否在项目根目录
check_directory() {
    if [[ ! -f "pom.xml" ]]; then
        print_error "请在项目根目录运行此脚本"
        exit 1
    fi
}

# 清理和编译
clean_and_compile() {
    print_header "清理和编译项目"
    mvn clean compile -q
    if [[ $? -eq 0 ]]; then
        print_message "编译成功"
    else
        print_error "编译失败"
        exit 1
    fi
}

# 运行测试
run_tests() {
    print_header "运行测试"
    mvn test -q
    if [[ $? -eq 0 ]]; then
        print_message "测试通过"
    else
        print_warning "测试失败，继续发布..."
    fi
}

# 发布到本地仓库
deploy_to_local() {
    print_header "发布到本地Maven仓库"
    mvn clean install -DskipTests
    if [[ $? -eq 0 ]]; then
        print_message "已发布到本地仓库 (~/.m2/repository)"
    else
        print_error "本地发布失败"
        exit 1
    fi
}

# 发布到GitHub Packages
deploy_to_github() {
    print_header "发布到GitHub Packages"

    # 检查是否有GitHub配置
    if [[ ! -f "pom-github.xml" ]]; then
        print_error "pom-github.xml 文件不存在"
        exit 1
    fi

    # 检查是否有GitHub Token
    if [[ -z "$GITHUB_TOKEN" ]]; then
        print_warning "请设置 GITHUB_TOKEN 环境变量"
        print_message "export GITHUB_TOKEN=your_github_token"
    fi

    mvn -f pom-github.xml clean deploy -DskipTests
    if [[ $? -eq 0 ]]; then
        print_message "已发布到GitHub Packages"
        print_message "访问地址: https://github.com/mcallzbl/common/packages"
    else
        print_error "GitHub发布失败"
        exit 1
    fi
}

# 发布到Maven Central
deploy_to_central() {
    print_header "发布到Maven Central (需要GPG签名)"

    # 检查是否有Maven Central配置
    if [[ ! -f "pom-central.xml" ]]; then
        print_error "pom-central.xml 文件不存在"
        exit 1
    fi

    # 检查是否有GPG密钥
    if ! gpg --list-secret-keys >/dev/null 2>&1; then
        print_error "未找到GPG密钥，请先生成GPG密钥"
        print_message "生成命令: gpg --gen-key"
        exit 1
    fi

    # 检查是否有Sonatype账号
    print_warning "请确保已配置 ~/.m2/settings.xml 中的Sonatype账号信息"

    mvn -f pom-central.xml clean deploy -P release -DskipTests
    if [[ $? -eq 0 ]]; then
        print_message "已发布到Maven Central"
        print_message "请到 https://s01.oss.sonatype.org/ 检查并release"
    else
        print_error "Maven Central发布失败"
        exit 1
    fi
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  local     发布到本地Maven仓库"
    echo "  github    发布到GitHub Packages"
    echo "  central   发布到Maven Central"
    echo "  all       发布到所有仓库"
    echo "  help      显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 local     # 发布到本地"
    echo "  $0 github    # 发布到GitHub"
    echo "  $0 central   # 发布到Maven Central"
}

# 主函数
main() {
    check_directory

    case "${1:-help}" in
        "local")
            clean_and_compile
            run_tests
            deploy_to_local
            ;;
        "github")
            clean_and_compile
            run_tests
            deploy_to_github
            ;;
        "central")
            clean_and_compile
            run_tests
            deploy_to_central
            ;;
        "all")
            clean_and_compile
            run_tests
            deploy_to_local
            deploy_to_github
            deploy_to_central
            ;;
        "help"|*)
            show_help
            ;;
    esac
}

# 执行主函数
main "$@"