package com.mcallzbl.user.interceptor;

import com.mcallzbl.user.context.IpContext;
import com.mcallzbl.user.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * IP地址拦截器
 * 在请求处理前获取客户端IP地址并存储到ThreadLocal上下文中
 * 请求完成后自动清理，防止内存泄漏
 *
 * @author mcallzbl
 * @since 2025-11-01
 */
@Slf4j
@Component
public class IpInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler) throws Exception {
        try {
            // 获取客户端真实IP地址
            String clientIp = IpUtils.getClientIpAddress(request);

            // 将IP地址存储到ThreadLocal上下文中
            IpContext.setIp(clientIp);

            log.debug("IP拦截器：已设置请求IP地址 - URI: {}, IP: {}, IP类型: {}",
                    request.getRequestURI(),
                    clientIp,
                    IpUtils.isInternalIp(clientIp) ? "内网" : "外网");

            return true;

        } catch (Exception e) {
            log.error("IP拦截器获取IP地址失败", e);
            // 即使获取IP失败，也不应该阻止请求继续执行
            // 设置一个默认IP地址
            IpContext.setIp("127.0.0.1");
            return true;
        }
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                              @NonNull HttpServletResponse response,
                              @NonNull Object handler,
                              Exception ex) throws Exception {
        try {
            // 请求完成后清理ThreadLocal，防止内存泄漏
            String ip = IpContext.getIp();
            if (ip != null) {
                log.debug("IP拦截器：清理请求IP地址 - URI: {}, IP: {}", request.getRequestURI(), ip);
            }
            IpContext.clear();

        } catch (Exception e) {
            log.error("IP拦截器清理ThreadLocal失败", e);
            // 强制清理
            try {
                IpContext.clear();
            } catch (Exception ignored) {
                // 忽略清理失败
            }
        }
    }
}