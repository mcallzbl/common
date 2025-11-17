CREATE TABLE `user`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
    `username`        VARCHAR(20)  NOT NULL COMMENT '用户名，登录用',
    `email`           VARCHAR(254) NOT NULL COMMENT '邮箱地址，用于登录和通知',
    `phone`           VARCHAR(20)           DEFAULT NULL COMMENT '手机号码，可选',
    `password_hash`   VARCHAR(255) NOT NULL COMMENT '密码，加密存储',
    `nickname`        VARCHAR(50)           DEFAULT NULL COMMENT '用户昵称，显示名称',
    `avatar_url`      VARCHAR(255)          DEFAULT NULL COMMENT '头像URL地址',
    `gender`          TINYINT               DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `birthday`        DATE                  DEFAULT NULL COMMENT '生日',
    `status`          TINYINT      NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常，2-冻结',
    `last_login_time` DATETIME              DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip`   VARCHAR(45)           DEFAULT NULL COMMENT '最后登录IP地址',
    `login_count`     INT                   DEFAULT 0 COMMENT '登录次数统计',
    `email_verified`  TINYINT               DEFAULT 0 COMMENT '邮箱是否已验证：0-未验证，1-已验证',
    `phone_verified`  TINYINT               DEFAULT 0 COMMENT '手机是否已验证：0-未验证，1-已验证',
#     `two_factor_enabled` TINYINT DEFAULT 0 COMMENT '是否开启双因素认证：0-关闭，1-开启',
    `timezone`        VARCHAR(50)           DEFAULT 'Asia/Shanghai' COMMENT '用户时区设置',
    `language`        VARCHAR(10)           DEFAULT 'zh-CN' COMMENT '用户语言偏好',
    `is_deleted`      TINYINT      NOT NULL DEFAULT 0 COMMENT '是否删除：0-正常，1-已删除',
    `deleted_time`    DATETIME              DEFAULT NULL COMMENT '删除时间，NULL表示未删除',
    `deleted_reason`  VARCHAR(500)          DEFAULT NULL COMMENT '删除原因',
    `created_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username_deleted` (`username`, `deleted_time`),
    UNIQUE KEY `uk_email_deleted` (`email`, `deleted_time`),
    KEY `idx_phone` (`phone`),
    KEY `idx_status` (`status`),
    KEY `idx_created_time` (`created_time`),
    KEY `idx_last_login_time` (`last_login_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户信息表';

-- 用户角色关联表（支持多角色）
CREATE TABLE `user_role`
(
    `id`           BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`      BIGINT   NOT NULL COMMENT '用户ID',
    `role_id`      BIGINT   NOT NULL COMMENT '角色ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户角色关联表';

-- 角色表
CREATE TABLE `role`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT COMMENT '角色ID，主键',
    `role_name`    VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code`    VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description`  VARCHAR(200)         DEFAULT NULL COMMENT '角色描述',
    `status`       TINYINT     NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='角色信息表';