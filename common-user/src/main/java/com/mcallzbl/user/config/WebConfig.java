package com.mcallzbl.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 * 注意：原有的拦截器配置已迁移到Spring Security的SecurityConfig中
 *
 * @author mcallzbl
 * @version 2.0
 * @since 2025/11/1
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 注意：
    // 1. JWT认证拦截器 -> 已迁移到JwtAuthenticationFilter（Spring Security过滤器）
    // 2. IP地址拦截器 -> 已迁移到IpAuthenticationFilter（Spring Security过滤器）
    // 3. CORS配置 -> 已迁移到SecurityConfig.corsConfigurationSource()

    // 如果需要添加参数解析器、消息转换器等配置，可以在这里添加
    // 例如：
    // @Override
    // public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    //     resolvers.add(currentUserArgumentResolver);
    // }
}