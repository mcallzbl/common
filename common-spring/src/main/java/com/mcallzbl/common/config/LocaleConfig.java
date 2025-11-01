package com.mcallzbl.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

/**
 * 本地化配置类
 * 配置多语言支持的解析器
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Configuration
public class LocaleConfig {

    /**
     * 配置Locale解析器
     * 基于Accept-Language请求头解析用户语言偏好
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();

        // 设置默认语言为中文
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);

        // 设置支持的语言列表
        resolver.setSupportedLocales(Arrays.asList(
                Locale.SIMPLIFIED_CHINESE,  // zh_CN
                Locale.ENGLISH,             // en
                Locale.JAPAN                // ja_JP
        ));

        return resolver;
    }
}