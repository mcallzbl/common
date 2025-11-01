package com.mcallzbl.user.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户模块条件配置类
 * 检查必要的外部依赖是否存在，如果都存在则导入用户模块组件扫描配置
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Slf4j
@AutoConfiguration(after = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class, MailSenderAutoConfiguration.class})
@MapperScan("com.mcallzbl.user.mapper")
@ComponentScan(basePackages = "com.mcallzbl.user")
public class UserAutoConfiguration {

    public UserAutoConfiguration() {
        log.info("用户模块注册完毕");
    }
}