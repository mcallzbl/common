package com.mcallzbl.user.Interceptor;

import com.mcallzbl.user.constants.AuthConstants;
import com.mcallzbl.user.context.UserContext;
import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.service.UserService;
import com.mcallzbl.user.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String token = extractTokenFromRequest(request);

        if (!StringUtils.hasText(token)) {
            sendUnauthorizedError(response, "Token无效或已过期");
            // 对于需要认证的接口，如果没有token，则直接拒绝
            // 这里可以根据注解或路径判断是否需要认证，为简化起见，我们假设所有带token的都是需要认证的
            // 如果某些接口可选认证，则这里应该直接 return true
            return false;
        }

        try {
            Long userId = Long.valueOf(jwtUtil.extractSubject(token));
            User user = userService.getUserById(userId);

            if (user == null || !user.isActive()) {
                log.warn("JWT认证失败：用户不存在或状态异常。用户ID: {}", userId);
                sendUnauthorizedError(response, "用户不存在或状态异常");
                return false;
            }

            // 认证成功，将用户信息存储到ThreadLocal
            UserContext.setCurrentUser(user);
            log.debug("JWT认证成功：用户ID={}, URI={}",
                    user.getId(), request.getRequestURI());

            return true;

        } catch (Exception e) {
            log.warn("JWT认证失败：{}, URI={}", e.getMessage(), request.getRequestURI());
            sendUnauthorizedError(response, "Token无效或已过期");
            return false;
        }
    }

    private void sendUnauthorizedError(HttpServletResponse response, String message) throws java.io.IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"code\": 401, \"message\": \"%s\"}", message));
    }

    private void sendForbiddenError(HttpServletResponse response, String message) throws java.io.IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"code\": 403, \"message\": \"%s\"}", message));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后清除ThreadLocal，防止内存泄漏
        UserContext.clear();
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
            return authHeader.substring(7);
        }

        // 2. 从请求参数获取（用于某些特殊场景）
        String tokenParam = request.getParameter("access_token");
        if (StringUtils.hasText(tokenParam)) {
            return tokenParam;
        }

        return null;
    }
}
