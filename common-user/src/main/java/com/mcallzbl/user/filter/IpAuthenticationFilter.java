package com.mcallzbl.user.filter;

import com.mcallzbl.user.context.IpContext;
import com.mcallzbl.user.utils.IpUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * IP地址过滤器
 * 替换原有的IpInterceptor，集成到Spring Security过滤器链中
 * 在请求处理前获取客户端IP地址并存储到ThreadLocal上下文中
 *
 * @author mcallzbl
 * @since 2025-11-17
 */
@Slf4j
@Component
public class IpAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // 获取客户端真实IP地址
            String clientIp = IpUtils.getClientIpAddress(request);

            // 将IP地址存储到ThreadLocal上下文中
            IpContext.setIp(clientIp);

            log.debug("IP过滤器：已设置请求IP地址 - URI: {}, IP: {}, IP类型: {}",
                    request.getRequestURI(),
                    clientIp,
                    IpUtils.isInternalIp(clientIp) ? "内网" : "外网");

        } catch (Exception e) {
            log.error("IP过滤器获取IP地址失败", e);
            // 即使获取IP失败，也不应该阻止请求继续执行
            // 设置一个默认IP地址
            IpContext.setIp("127.0.0.1");
        }

        try {
            // 继续过滤器链
            filterChain.doFilter(request, response);
        } finally {
            // 使用finally确保清理ThreadLocal
            try {
                String ip = IpContext.getIp();
                if (ip != null) {
                    log.debug("IP过滤器：清理请求IP地址 - URI: {}, IP: {}", request.getRequestURI(), ip);
                }
                IpContext.clear();
            } catch (Exception e) {
                log.error("IP过滤器清理ThreadLocal失败", e);
                // 强制清理
                try {
                    IpContext.clear();
                } catch (Exception ignored) {
                    // 忽略清理失败
                }
            }
        }
    }
}