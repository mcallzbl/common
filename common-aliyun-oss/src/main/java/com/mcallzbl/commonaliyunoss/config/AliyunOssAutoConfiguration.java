package com.mcallzbl.commonaliyunoss.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/15
 */
@Slf4j
@AutoConfiguration
@ComponentScan(basePackages = "com.mcallzbl.commonaliyunoss")
public class AliyunOssAutoConfiguration {
    public AliyunOssAutoConfiguration() {
        log.info("阿里云对象存储模块注册完毕");
    }
}
