package com.mcallzbl.user.filter;

import com.mcallzbl.user.constants.AuthConstants;
import com.mcallzbl.user.context.UserContext;
import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.security.UserDetailsImpl;
import com.mcallzbl.user.service.UserService;
import com.mcallzbl.user.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 替换原有的JwtAuthInterceptor，集成到Spring Security过滤器链中
 *
 * @author mcallzbl
 * @since 2025-11-17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractTokenFromRequest(request);

            if (StringUtils.hasText(token) && !jwtUtil.isTokenExpired(token)) {
                Long userId = Long.valueOf(jwtUtil.extractSubject(token));
                User user = userService.getUserById(userId);

                if (user != null && !user.isInActive()) {
                    // 创建用户详情对象
                    UserDetailsImpl userDetails = new UserDetailsImpl(user);

                    // 创建认证令牌
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );

                    // 设置认证详情
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 设置到Spring Security上下文
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // 兼容现有代码，将用户信息存储到ThreadLocal
                    UserContext.setCurrentUser(user);

                    log.debug("JWT认证成功：用户ID={}, URI={}", user.getId(), request.getRequestURI());
                } else {
                    log.warn("JWT认证失败：用户不存在或状态异常。用户ID: {}", userId);
                }
            }
        } catch (Exception e) {
            log.warn("JWT认证失败：{}, URI={}", e.getMessage(), request.getRequestURI());
            // 不抛出异常，让过滤器链继续执行，由Spring Security处理未认证的情况
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 确保在过滤器链执行完成后清理ThreadLocal
            UserContext.clear();
        }
    }

    /**
     * 从请求中提取JWT Token
     *
     * @param request HTTP请求
     * @return JWT Token，如果不存在返回null
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        // 1. 从Authorization header获取
        String authHeader = request.getHeader(AuthConstants.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(AuthConstants.BEARER)) {
            return authHeader.substring(AuthConstants.BEARER_LENGTH);
        }

        // 2. 从请求参数获取（用于某些特殊场景）
        String tokenParam = request.getParameter("access_token");
        if (StringUtils.hasText(tokenParam)) {
            return tokenParam;
        }

        return null;
    }
}