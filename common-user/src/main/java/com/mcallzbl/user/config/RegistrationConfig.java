package com.mcallzbl.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用户注册配置
 * 控制不同注册方式的开启和关闭
 *
 * @author mcallzbl
 * @since 2025-11-17
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.registration")
public class RegistrationConfig {

    /**
     * 是否启用用户名密码注册
     * 默认关闭，防止恶意注册
     */
    private boolean usernamePasswordEnabled = false;

    /**
     * 是否启用邮箱验证码注册
     * 默认开启，通过验证码验证邮箱真实性
     */
    private boolean emailVerificationEnabled = true;

    /**
     * 注册频率限制（同一IP每小时最大注册次数）
     * 默认5次，防止恶意批量注册
     */
    private int rateLimitPerHour = 5;

    /**
     * 注册频率限制（同一邮箱每天最大注册次数）
     * 默认1次，防止邮箱滥用
     */
    private int emailRateLimitPerDay = 1;

    /**
     * 用户名注册时邮箱是否必填
     * 默认必填，提供完整的用户信息
     */
    private boolean emailRequired = true;

    /**
     * 是否检查用户名唯一性
     * 默认开启，确保用户名不重复
     */
    private boolean checkUsernameUnique = true;

    /**
     * 是否检查邮箱唯一性
     * 默认开启，确保邮箱不重复
     */
    private boolean checkEmailUnique = true;

    /**
     * 检查用户名唯一性配置
     */
    public boolean isCheckUsernameUnique() {
        return checkUsernameUnique;
    }

    /**
     * 检查邮箱唯一性配置
     */
    public boolean isCheckEmailUnique() {
        return checkEmailUnique;
    }

    /**
     * 检查邮箱是否必填配置
     */
    public boolean isEmailRequired() {
        return emailRequired;
    }
}