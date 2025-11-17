package com.mcallzbl.user.config;

import com.mcallzbl.user.filter.IpAuthenticationFilter;
import com.mcallzbl.user.filter.JwtAuthenticationFilter;
import com.mcallzbl.user.handler.CustomAccessDeniedHandler;
import com.mcallzbl.user.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security安全配置
 * 替换原有的拦截器，使用Spring Security过滤器链
 *
 * @author mcallzbl
 * @version 2.0
 * @since 2025/11/2
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final IpAuthenticationFilter ipAuthenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 密码编码器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security过滤器链配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF，因为我们使用JWT进行认证
                .csrf(AbstractHttpConfigurer::disable)

                // 配置CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 无状态会话管理
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 禁用默认登录页面
                .formLogin(AbstractHttpConfigurer::disable)

                // 禁用HTTP Basic认证
                .httpBasic(AbstractHttpConfigurer::disable)

                // 禁用默认登出
                .logout(AbstractHttpConfigurer::disable)

                // 异常处理
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)  // 权限不足处理
                        .authenticationEntryPoint(authenticationEntryPoint)  // 未认证处理
                )

                // 请求授权配置 - 对应原WebConfig中的excludePathPatterns
                .authorizeHttpRequests(authz -> authz
                                // 公开访问的端点（不需要认证）
                                .requestMatchers("/api/v1/auth/**").permitAll()
//                        .requestMatchers("/api/v1/auth/registration").permitAll()
//                        .requestMatchers("/api/v1/auth/verification/emails").permitAll()
//                        .requestMatchers("/api/v1/auth/refresh").permitAll()
//                        .requestMatchers("/api/v1/auth/logout").permitAll()
                                .requestMatchers("/api/v1/captcha").permitAll()

                                // API文档相关
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/knife4j/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll()

                                // 其他所有请求都需要认证
                                .anyRequest().authenticated()
                )

                // 添加自定义过滤器 - 对应原拦截器的优先级
                // IP过滤器优先级最高（order=0）- 对应原ipInterceptor order=0
                .addFilterBefore(ipAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // JWT认证过滤器（order=1）- 对应原jwtAuthInterceptor order=1
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
