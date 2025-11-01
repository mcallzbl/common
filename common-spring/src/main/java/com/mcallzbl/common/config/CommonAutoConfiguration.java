package com.mcallzbl.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcallzbl.common.exception.GlobalExceptionHandler;
import com.mcallzbl.common.exception.I18nBusinessException;
import com.mcallzbl.common.interceptor.GlobalResponseWrapper;
import com.mcallzbl.common.util.CommonI18nUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 公共模块自动配置类
 * 自动注册响应包装器和异常处理器
 */
@Configuration
@Slf4j
@ConditionalOnWebApplication
public class CommonAutoConfiguration {

    /**
     * 注册全局响应包装器
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalResponseWrapper globalResponseWrapper(ObjectMapper objectMapper) {
        log.info("注册全局响应包装器");
        return new GlobalResponseWrapper(objectMapper);
    }

    /**
     * 注册全局异常处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        log.info("注册全局异常处理器");
        return new GlobalExceptionHandler();
    }

    // ==================== 国际化配置自动注册 ====================

    /**
     * 注册MessageSource Bean（用于国际化）
     */
    @Bean
    @ConditionalOnMissingBean(MessageSource.class)
    public MessageSource messageSource() {
        log.info("注册MessageSource Bean（国际化支持）");
        InternationalizationConfig config = new InternationalizationConfig();
        return config.messageSource();
    }

    /**
     * 注册LocaleResolver Bean（用于解析请求语言）
     */
    @Bean
    @ConditionalOnMissingBean(name = "localeResolver")
    public org.springframework.web.servlet.LocaleResolver localeResolver() {
        log.info("注册LocaleResolver Bean（语言解析器）");
        LocaleConfig config = new LocaleConfig();
        return config.localeResolver();
    }

    /**
     * 注册LocalValidatorFactoryBean Bean（用于JSR-303验证国际化）
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalValidatorFactoryBean validator() {
        log.info("注册LocalValidatorFactoryBean Bean（验证国际化）");
        InternationalizationConfig config = new InternationalizationConfig();
        return config.validator();
    }

    /**
     * 注册I18nUtils工具类Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public CommonI18nUtils i18nUtils(MessageSource messageSource) {
        log.info("注册I18nUtils Bean（国际化工具类）");
        return new CommonI18nUtils(messageSource);
    }

    /**
     * 注册I18nBusinessException Bean（国际化异常处理器）
     */
    @Bean
    @ConditionalOnMissingBean
    public I18nBusinessException i18nBusinessException(CommonI18nUtils commonI18NUtils) {
        log.info("注册I18nBusinessException Bean（国际化异常处理器）");
        return new I18nBusinessException(commonI18NUtils);
    }
}