**[🇨🇳 中文](#-个人自用---快速启动-spring-boot-项目的公共组件库)** | **English**

# Common Components Library | Spring Boot Starter Template | Java Components

> 🔧 **Personal Use** - Quick start Spring Boot project component library

> **📦 What you'll get:**
> - ⚡ **Unified API Response Wrapper** - Standardize your API responses instantly
> - 🛡️ **Global Exception Handler** - Handle errors gracefully across your application
> - 👤 **Ready-to-Use Email Login Module** - Complete authentication system out of the box
> - 🌍 **Internationalization Support** - Multi-language ready
> - ☁️ **File Upload with Aliyun OSS** - High-performance cloud storage

⚠️ **Important Notice**: This project is **not published** to Maven Central Repository. It's a personal collection of reusable components designed for quick project startup through source code copying.

**Keywords**: Spring Boot, Java 17, Maven, Authentication, JWT, File Upload, User Management, API Template, Microservices, Spring Security, MyBatis Plus, OSS, Common Library, Components Library, Quick Start

## 🤝 Community & Feedback

**This project is primarily for personal use, but I warmly welcome:**
- 🔍 **Code reviews** and design feedback
- 💡 **Better suggestions** for architecture improvements
- 🐛 **Bug reports** and issue identification
- 🚀 **Feature requests** and enhancement ideas
- 📝 **Documentation improvements**

**Feel free to open an issue or submit a pull request if you find any design flaws, have suggestions for improvements, or want to contribute!**

---

## 🏗️ Project Architecture

### Maven Multi-Module Structure
```
common-parent/
├── common-spring/        # 🔥 Spring Boot core functionality module
├── common-user/          # 👤 User authentication & authorization module
├── common-aliyun-oss/    # ☁️ Aliyun OSS file upload module
└── common-demo/          # 📚 Example demonstration module
```

### Tech Stack
- **Java 17** - Modern Java features
- **Spring Boot 3.5.6** - Latest stable version
- **MyBatis Plus 3.5.12** - Database ORM
- **JWT 0.12.6** - Token authentication
- **Knife4j 4.5.0** - API documentation
- **Hutool 5.8.26** - Utility library

## 📦 Module Features

### 1. 🔥 common-spring (Core Foundation Module)

**Provides basic architecture and common functionality for Spring Boot applications**

#### 🎯 Core Features

**📡 Unified Response Wrapper System**
- `@ResponseWrapper` annotation automatically wraps API responses
- Standardized JSON response format
- Supports class-level and method-level control
- Intelligent handling of different return types

**🛡️ Powerful Exception Handling Mechanism**
- Global exception handler
- Classified error code system (User: 1001+, Captcha: 2001+, Token: 3001+, Feature: 4001+)
- Supports exception chaining (message + cause)
- User-friendly error messages

**🌍 Complete Internationalization Support**
- Automatic language detection (Accept-Language)
- Supports Chinese, English, Japanese
- Parameterized message templates
- Internationalized exception messages

**⚙️ Development Convenience Tools**
- `BaseEntity` unified entity base class
- Automatic time filling (Instant type)
- MyBatis Plus integration configuration
- Conditional auto-configuration

#### 📋 Dependency Introduction
```xml
<dependency>
    <groupId>com.mcallzbl</groupId>
    <artifactId>common-spring</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

---

### 2. 👤 common-user (User Authentication & Authorization Module)

**Provides complete user authentication, authorization, and management functionality**

#### 🎯 Core Features

**🔐 Multi-Method Authentication System**
- Email + password login
- Email + verification code login
- Username + password login
- Username registration process

**🎫 JWT Token Management**
- Dual Token mechanism (Access + Refresh)
- Automatic Token refresh
- HttpOnly Cookie secure storage
- Complete Token lifecycle management

**🛡️ Security Protection System**
- Spring Security integration
- BCrypt password encryption
- IP address acquisition and restriction
- Role-based access control (`@RequireRole`)

**📧 Email Verification Service**
- Verification code sending and verification
- Thymeleaf email templates
- Multiple verification scenarios support

**👥 User Information Management**
- Complete user information model
- Login record tracking
- User status management
- Soft delete protection

#### 📋 Dependency Introduction
```xml
<dependency>
    <groupId>com.mcallzbl</groupId>
    <artifactId>common-user</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### ⚙️ Configuration Example
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-password

jwt:
  secret: your-secret-key
  access-token-expiration: 3600000  # 1 hour
  refresh-token-expiration: 604800000  # 7 days
```

---

### 3. ☁️ common-aliyun-oss (File Upload Module)

**Provides Aliyun OSS client-side direct file upload functionality**

#### 🎯 Core Features

**🚀 High-Performance Upload**
- Client-side direct upload mode (STS temporary credentials)
- Bypasses server relay, reduces server load
- Supports concurrent uploads

**📁 Multi-Type File Support**
- 📸 **Images** (jpg, png, gif, etc., limit 10MB)
- 🎥 **Videos** (mp4, avi, mov, etc., limit 500MB)
- 📄 **Documents** (pdf, doc, excel, etc., limit 50MB)
- 🎵 **Audio** (mp3, wav, flac, etc., limit 100MB)

**🔒 Security Features**
- STS temporary credential mechanism
- User-level session isolation
- Role-based access control

**📂 Intelligent File Organization**
- Automatic classification by type and date
- UUID filenames to prevent conflicts
- Standardized path structure

#### 📋 Dependency Introduction
```xml
<dependency>
    <groupId>com.mcallzbl</groupId>
    <artifactId>common-aliyun-oss</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### ⚙️ Configuration Example
```yaml
aliyun:
  oss:
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    endpoint: https://oss-cn-beijing.aliyuncs.com
    bucket: your-bucket-name
    upload-path: uploads
  sts:
    role-arn: acs:ram::your-account-id:role/your-role
    region: cn-beijing
```

---

## 🚀 Quick Start

### 1. Clone Project
```bash
git clone <repository-url>
cd common
```

### 2. Install to Local Repository
```bash
mvn clean install
```

### 3. Add Dependencies to New Project
Select appropriate modules to include in your new project's `pom.xml`

### 4. Copy Configuration Files
Copy relevant configurations from `common-demo` module to your new project and modify as needed

## 📚 Usage Guide

### Copy-Paste Usage Method

Each module is designed to be used independently and supports:

1. **Source Code Copy** - Directly copy Java source files to new project
2. **Dependency Integration** - Import through Maven dependencies
3. **Configuration Reuse** - Copy related configuration files and templates

### Recommended Usage Order

1. **common-spring** (Required) - Provides basic architecture
2. **common-user** (As needed) - When user authentication is required
3. **common-aliyun-oss** (As needed) - When file upload is required

## 📖 API Documentation

After starting the `common-demo` application, visit:
- **Knife4j Documentation**: http://localhost:8080/doc.html
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 🎯 Design Philosophy

- **Modular Design** - Each module has a single responsibility and can be used independently
- **Zero Configuration Principle** - Provides reasonable default configurations to reduce setup work
- **Out of the Box** - Ready to use through copying, no complex integration required
- **Production Ready** - Based on best practices, suitable for production environments
- **Continuous Optimization** - Continuously improved based on actual usage needs

## 📋 Version Information

- **Java**: 17+
- **Spring Boot**: 3.5.6
- **Current Version**: 1.0-SNAPSHOT
- **Author**: mcallzbl
- **Created**: 2025-11-20

---

*💡 **Tip**: This is a personal rapid development tool library designed primarily to improve new project startup efficiency. All modules have been validated in actual projects and can be used with confidence.*

*🔄 **Updates**: Continuously expanded and optimized based on new project requirements.*

---

> 🔧 **个人自用** - 快速启动 Spring Boot 项目的公共组件库

> **📦 你将获得:**
> - ⚡ **统一 API 响应包装** - 立即标准化你的API响应格式
> - 🛡️ **全局异常处理** - 在整个应用中优雅地处理错误
> - 👤 **开箱即用的邮箱登录模块** - 完整的认证系统
> - 🌍 **国际化支持** - 多语言就绪
> - ☁️ **阿里云 OSS 文件上传** - 高性能云存储

⚠️ **重要提醒**: 此项目**未上传**到Maven中央仓库，是个人自用的公共组件库，通过源码复制方式快速启动项目。

## 🤝 社区与反馈

**这个项目主要用于个人使用，但我热忱欢迎：**
- 🔍 **代码审查**和设计反馈
- 💡 **架构改进**的更好建议
- 🐛 **问题报告**和缺陷识别
- 🚀 **功能请求**和增强想法
- 📝 **文档改进**建议

**如果您发现任何设计缺陷、有改进建议或想要贡献，欢迎随时提交 Issue 或 Pull Request！**

---

## 🏗️ 项目架构

### Maven 多模块结构
```
common-parent/
├── common-spring/        # 🔥 Spring Boot 核心功能模块
├── common-user/          # 👤 用户认证授权模块
├── common-aliyun-oss/    # ☁️ 阿里云 OSS 文件上传模块
└── common-demo/          # 📚 示例演示模块
```

### 技术栈
- **Java 17** - 现代 Java 特性
- **Spring Boot 3.5.6** - 最新稳定版本
- **MyBatis Plus 3.5.12** - 数据库 ORM
- **JWT 0.12.6** - Token 认证
- **Knife4j 4.5.0** - API 文档
- **Hutool 5.8.26** - 工具类库

## 📦 模块功能介绍

### 1. 🔥 common-spring (核心基础模块)

**提供 Spring Boot 应用的基础架构和通用功能**

#### 🎯 核心特性

**📡 统一响应包装系统**
- `@ResponseWrapper` 注解自动包装 API 响应
- 标准化的 JSON 响应格式
- 支持类级别和方法级别控制
- 智能处理不同返回类型

**🛡️ 强大的异常处理机制**
- 全局异常处理器
- 分类错误码体系 (用户: 1001+, 验证码: 2001+, Token: 3001+, 功能: 4001+)
- 支持异常链 (message + cause)
- 用户友好的错误消息

**🌍 完整的国际化支持**
- 自动语言检测 (Accept-Language)
- 支持中文、英文、日文
- 参数化消息模板
- 国际化异常消息

**⚙️ 开发便利工具**
- `BaseEntity` 统一实体基类
- 自动时间填充 (Instant 类型)
- MyBatis Plus 集成配置
- 条件化自动装配

#### 📋 依赖引入
```xml
<dependency>
    <groupId>com.mcallzbl</groupId>
    <artifactId>common-spring</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

---

### 2. 👤 common-user (用户认证授权模块)

**提供完整的用户认证、授权和管理功能**

#### 🎯 核心特性

**🔐 多方式认证系统**
- 邮箱 + 密码登录
- 邮箱 + 验证码登录
- 用户名 + 密码登录
- 用户名注册流程

**🎫 JWT Token 管理**
- 双 Token 机制 (Access + Refresh)
- 自动 Token 刷新
- HttpOnly Cookie 安全存储
- 完整的 Token 生命周期管理

**🛡️ 安全防护体系**
- Spring Security 集成
- BCrypt 密码加密
- IP 地址获取和限制
- 角色权限控制 (`@RequireRole`)

**📧 邮箱验证服务**
- 验证码发送和验证
- Thymeleaf 邮件模板
- 多种验证场景支持

**👥 用户信息管理**
- 完整的用户信息模型
- 登录记录追踪
- 用户状态管理
- 软删除保护

#### 📋 依赖引入
```xml
<dependency>
    <groupId>com.mcallzbl</groupId>
    <artifactId>common-user</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### ⚙️ 配置示例
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-password

jwt:
  secret: your-secret-key
  access-token-expiration: 3600000  # 1小时
  refresh-token-expiration: 604800000  # 7天
```

---

### 3. ☁️ common-aliyun-oss (文件上传模块)

**提供阿里云 OSS 客户端直传文件上传功能**

#### 🎯 核心特性

**🚀 高性能上传**
- 客户端直传模式 (STS 临时凭证)
- 绕过服务端中转，减轻服务器压力
- 支持并发上传

**📁 多类型文件支持**
- 📸 **图片** (jpg, png, gif 等, 限制 10MB)
- 🎥 **视频** (mp4, avi, mov 等, 限制 500MB)
- 📄 **文档** (pdf, doc, excel 等, 限制 50MB)
- 🎵 **音频** (mp3, wav, flac 等, 限制 100MB)

**🔒 安全特性**
- STS 临时凭证机制
- 用户级别会话隔离
- 基于角色的访问控制

**📂 智能文件组织**
- 按类型和日期自动分类
- UUID 文件名防止冲突
- 标准化路径结构

#### 📋 依赖引入
```xml
<dependency>
    <groupId>com.mcallzbl</groupId>
    <artifactId>common-aliyun-oss</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### ⚙️ 配置示例
```yaml
aliyun:
  oss:
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    endpoint: https://oss-cn-beijing.aliyuncs.com
    bucket: your-bucket-name
    upload-path: uploads
  sts:
    role-arn: acs:ram::your-account-id:role/your-role
    region: cn-beijing
```

---

## 🚀 快速开始

### 1. 克隆项目
```bash
git clone <repository-url>
cd common
```

### 2. 安装到本地仓库
```bash
mvn clean install
```

### 3. 新项目中引入依赖
根据需要选择相应的模块引入到新项目的 `pom.xml` 中

### 4. 复制配置文件
从 `common-demo` 模块复制相关配置到新项目，并根据需要修改

## 📚 使用指南

### 复制粘贴使用方式

每个模块都设计为可以独立使用，支持：

1. **源码级复制** - 直接复制 Java 源文件到新项目
2. **依赖级集成** - 通过 Maven 依赖引入
3. **配置级复用** - 复制相关配置文件和模板

### 推荐使用顺序

1. **common-spring** (必须) - 提供基础架构
2. **common-user** (按需) - 需要用户认证时
3. **common-aliyun-oss** (按需) - 需要文件上传时

## 📖 API 文档

启动 `common-demo` 应用后，访问：
- **Knife4j 文档**: http://localhost:8080/doc.html
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 🎯 设计理念

- **模块化设计** - 每个模块职责单一，可独立使用
- **零配置原则** - 提供合理的默认配置，减少配置工作
- **开箱即用** - 复制即可使用，无需复杂集成
- **生产就绪** - 基于最佳实践，适合生产环境
- **持续优化** - 根据实际使用需求持续改进

## 📋 版本信息

- **Java**: 17+
- **Spring Boot**: 3.5.6
- **当前版本**: 1.0-SNAPSHOT
- **作者**: mcallzbl
- **创建时间**: 2025-11-20

---

*💡 **提示**: 这是一个个人自用的快速开发工具库，主要为了提高新项目启动效率。所有模块都经过实际项目验证，可以放心使用。*

*🔄 **更新**: 持续根据新项目需求进行功能扩展和优化改进。*

---

**[↑ Back to English](#readme)**