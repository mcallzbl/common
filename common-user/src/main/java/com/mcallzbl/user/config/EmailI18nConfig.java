package com.mcallzbl.user.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

/**
 * 邮件模块国际化配置类
 * 独立配置邮件相关的国际化消息源
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Configuration
public class EmailI18nConfig {

    /**
     * 配置邮件模块专用的MessageSource Bean
     * 只加载邮件相关的国际化文件
     */
    @Bean("emailMessageSource")
    @ConditionalOnMissingBean(name = "emailMessageSource")
    public MessageSource emailMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        // 设置邮件模块国际化文件的基础路径
        messageSource.setBasename("classpath:i18n/email");

        // 设置默认编码
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

        // 设置缓存时间（秒），-1表示永久缓存
        messageSource.setCacheSeconds(3600);

        // 如果找不到对应语言的文件，是否回退到系统默认语言
        messageSource.setFallbackToSystemLocale(false);

        // 如果找不到对应的key，是否使用key本身作为消息
        messageSource.setUseCodeAsDefaultMessage(false);

        return messageSource;
    }
}