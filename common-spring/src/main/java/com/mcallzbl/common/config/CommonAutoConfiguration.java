package com.mcallzbl.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcallzbl.common.exception.GlobalExceptionHandler;
import com.mcallzbl.common.interceptor.GlobalResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}