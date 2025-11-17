# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Maven multi-module common library project (`common-parent`) containing shared components for Spring Boot
applications. The project provides standardized response wrapping mechanisms and auto-configuration utilities.

## Architecture

### Module Structure

- **common-parent**: Root Maven project with dependency management
- **common-spring**: Spring Boot integration module containing:
    - Unified response wrapper system (`@ResponseWrapper` annotation)
    - Global response body advice (`GlobalResponseWrapper`)
    - Common result transfer DTO (`CommonResultTransferDTO`)
    - Business exception handling (`BusinessException`)
    - Standardized status codes (`ResultCode` enum)

### Response Wrapping System

Based on the Sora project architecture, all controller responses can be automatically wrapped using the
`@ResponseWrapper` annotation at class or method level. The system provides:

- **Standard Response Format**:
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": {},
    "timestamp": 1600000000000
  }
  ```

- **Status Code Strategy**:
    - HTTP status codes controlled via `httpStatus` field
    - Business logic status codes using `ResultCode` enum
    - Separate error code ranges for different business domains (user: 1001+, captcha: 2001+, token: 3001+, features:
      4001+)

## Build Commands

```bash
# Build entire project
mvn clean install

# Build specific module
mvn clean install -pl common-spring

# Run tests
mvn test

# Skip tests during build
mvn clean install -DskipTests

# Package without tests
mvn clean package -DskipTests
```

## Development Guidelines

- Java 17 is required
- Use Lombok for reducing boilerplate code
- Follow the established response wrapper pattern for all API endpoints
- Controllers should return business data objects directly - they will be auto-wrapped
- Throw `BusinessException` for business logic failures
- Use string format for timestamp fields in responses

## Dependencies

The project uses managed dependencies for:

- Lombok 1.18.42 (annotation processing)
- Spring Boot starter dependencies (to be added as needed)

## Project Configuration

- Source encoding: UTF-8
- Java version: 17
- Maven packaging: jar (for common-spring module)
- Parent POM manages dependency versions

数据库映射说明:
使用Mybatis plus
DateTime类型,大多数情况应该使用Instant作为映射
表示状态的类型，应该使用枚举类完成，并加上@EnumValue
时间字段不要加上自动填入

枚举设计：
涉及数据库映射，使用@EnumValue枚举，涉及json转换，就要@JsonValue和@JsonCreator
使用字符串转换成枚举类型时，最好不区分大小写
使用静态map完成转换

dto:
对于请求或返回类型，应该直接加上springdoc注解，并且请求的加上适当的参数验证

controller:
自动加上springdoc的注解
示例：

```java
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户认证相关接口，包括登录、注册、登出、刷新Token等")
```

## Entity Class Organization

### Base Entity Structure

All entity classes must extend `BaseEntity` for standardized audit fields and common functionality:

```java
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("table_name")
public class YourEntity extends BaseEntity {

    @TableId(value = "id")
    private Long id;

    // Business fields...

    /**
     * 软删除方法
     */
    public void softDelete(String reason) {
        this.deleteStatus = DeleteStatus.DELETED;
        this.deletedTime = Instant.now();
        this.deletedReason = reason;
    }
}
```

### DateTime Field Standards

- **Use `Instant` type** for all datetime fields to ensure timezone independence
- **Database JDBC URL** should specify timezone: `serverTimezone=UTC`
- **No automatic fill annotations** - handle timestamps manually in service layer
- **Time field mapping**: `Instant` ↔ `TIMESTAMP` or `DATETIME` columns

### Enum Usage Guidelines

**Database Mapping Enums** (for status fields, etc.):
```java
@Getter
public enum YourStatus {
    ACTIVE(1, "启用"),
    INACTIVE(0, "禁用");

    @EnumValue
    private final Integer code;
    private final String description;

    // Add string conversion with static map
    private static final Map<String, YourStatus> NAME_MAP = new ConcurrentHashMap<>();

    @JsonCreator
    public static YourStatus fromString(String value) {
        return NAME_MAP.get(value.toLowerCase().trim());
    }
}
```

**JSON Conversion Support**:
- Use `@JsonValue` for serialization
- Use `@JsonCreator` for deserialization
- Implement case-insensitive string conversion using static Map
- Always include `@EnumValue` for database mapping

### Entity Class Template

```java
package com.mcallzbl.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mcallzbl.common.BaseEntity;
import com.mcallzbl.common.enums.DeleteStatus;
import lombok.*;

import java.time.Instant;

/**
 * 实体类描述
 *
 * @author mcallzbl
 * @since 2025-MM-DD
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("table_name")
public class YourEntity extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 业务字段
     */
    @TableField(value = "field_name")
    private String fieldName;

    /**
     * 状态字段（使用枚举）
     */
    @TableField(value = "status")
    private YourStatus status = YourStatus.DEFAULT;

    /**
     * 时间字段（使用Instant）
     */
    @TableField(value = "created_time")
    private Instant createdTime;

}
```

通用：
自动在每个方法前加上注释
