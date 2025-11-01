package com.mcallzbl.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.nio.charset.StandardCharsets;

/**
 * 国际化配置类
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Configuration
public class InternationalizationConfig {

    /**
     * 配置MessageSource Bean
     * 用于获取国际化消息
     */
    @Bean("commonMessageSource")
    @ConditionalOnMissingBean(MessageSource.class)
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // 设置国际化文件的基础路径
        messageSource.setBasename("classpath:i18n/messages");
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

    /**
     * 配置本地化验证器
     * 用于JSR-303验证的国际化
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        return validator;
    }
}