package com.mcallzbl.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.session")
public class SessionConfig {
    private String domain = "localhost"; // 默认域名
    private boolean secure = false; // 是否启用HTTPS
    private int maxAge = 24 * 60 * 60; // Cookie有效期（秒）
}