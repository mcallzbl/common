# Common Spring Library 发布指南

## 概述

这个项目提供了统一的Spring Boot响应包装和异常处理功能，可以通过多种方式发布到Maven仓库。

## 发布方式

### 1. 本地Maven仓库（推荐用于开发测试）

最简单的方式，适合个人开发测试：

```bash
# 使用脚本发布
./deploy.sh local

# 或手动发布
mvn clean install -DskipTests
```

### 2. GitHub Packages（推荐用于开源项目）

免费、私有，与GitHub集成：

#### 准备工作：

1. 创建GitHub Personal Access Token
2. 设置环境变量：
   ```bash
   export GITHUB_TOKEN=your_github_token
   ```

#### 发布命令：

```bash
# 使用脚本发布
./deploy.sh github

# 或手动发布
mvn -f pom-github.xml clean deploy -DskipTests
```

#### 使用方式：

在项目的pom.xml中添加：

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/mcallzbl/common</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.mcallzbl</groupId>
        <artifactId>common-spring</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### 3. Maven Central（官方仓库）

需要审核，但所有人都可以使用：

#### 准备工作：

1. 注册Sonatype账号
2. 生成GPG密钥：
   ```bash
   gpg --gen-key
   gpg --list-keys --keyid-format LONG
   ```
3. 配置`~/.m2/settings.xml`：
   ```xml
   <servers>
       <server>
           <id>ossrh</id>
           <username>your-sonatype-username</username>
           <password>your-sonatype-password</password>
       </server>
   </servers>
   ```

#### 发布命令：

```bash
# 使用脚本发布
./deploy.sh central

# 或手动发布
mvn -f pom-central.xml clean deploy -P release -DskipTests
```

#### 发布后操作：

1. 访问 https://s01.oss.sonatype.org/
2. 搜索你的artifact
3. 检查并手动release
4. 等待同步到Maven Central（通常1-2小时）

## 发布脚本使用

项目提供了便捷的发布脚本：

```bash
# 查看帮助
./deploy.sh help

# 发布到本地仓库
./deploy.sh local

# 发布到GitHub Packages
./deploy.sh github

# 发布到Maven Central
./deploy.sh central

# 发布到所有仓库
./deploy.sh all
```

## 版本管理

### 发布前检查：

- [ ] 更新版本号（去掉SNAPSHOT）
- [ ] 更新CHANGELOG.md
- [ ] 运行所有测试
- [ ] 检查文档完整性

### 版本号格式：

- 开发版本：`1.0.0-SNAPSHOT`
- 稳定版本：`1.0.0`
- 补丁版本：`1.0.1`
- 次版本：`1.1.0`
- 主版本：`2.0.0`

## 故障排除

### GitHub Packages发布失败：

- 检查GITHUB_TOKEN是否设置
- 检查仓库名称是否正确
- 确保有push权限

### Maven Central发布失败：

- 检查GPG密钥配置
- 检查settings.xml配置
- 确保版本号不包含SNAPSHOT

### 编译失败：

- 检查Java版本（需要17+）
- 检查依赖版本冲突
- 运行`mvn dependency:tree`查看依赖树

## 项目信息

- **GroupId**: `com.mcallzbl` (GitHub) / `io.github.mcallzbl` (Maven Central)
- **ArtifactId**: `common-spring`
- **Java版本**: 17+
- **Spring Boot版本**: 3.x

## 联系方式

如有问题，请提交Issue或联系：mcallzbl@example.com