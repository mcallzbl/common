package com.mcallzbl.user.config;

import com.mcallzbl.user.Interceptor.JwtAuthInterceptor;
import com.mcallzbl.user.interceptor.IpInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;
    private final IpInterceptor ipInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // JWT认证拦截器 - 优先级高，主要认证方式
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/api/v1/auth/login",              // 登录接口不拦截
                        "/api/v1/auth/registration",       // 注册接口不拦截
                        "/api/v1/auth/verification/emails", // 邮件验证码不拦截
                        "/api/v1/auth/refresh",            // Token刷新不拦截
                        "/api/v1/auth/logout",             // 登出接口不拦截（内部会验证Token）
                        "/api/v1/captcha",                 // 图形验证码不拦截
                        "/error",                   // 错误页面不拦截
                        "/swagger-ui/**",           // Swagger UI不拦截
                        "/v3/api-docs/**",          // API文档不拦截
                        "/knife4j/**",              // Knife4j不拦截
                        "/actuator/**"              // Spring Boot Actuator不拦截
                )
                .order(1); // 设置优先级

        // IP地址拦截器 - 优先级最高，为所有请求设置IP上下文
        registry.addInterceptor(ipInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/error",                   // 错误页面不拦截
                        "/actuator/**"              // Spring Boot Actuator不拦截
                )
                .order(0); // 最高优先级，确保在其他拦截器之前执行

    }
}