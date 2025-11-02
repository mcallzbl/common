package com.mcallzbl.user.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * IP地址工具类
 * 用于获取客户端真实IP地址
 *
 * @author mcallzbl
 * @since 2025-11-01
 */
@Slf4j
public class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String LOCALHOST_IPV6_SHORT = "::1";
    private static final Pattern IP_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    /**
     * 获取客户端真实IP地址
     * 依次检查各种HTTP头部，最终使用request.getRemoteAddr()
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址，获取失败时返回127.0.0.1
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            log.warn("HttpServletRequest is null, returning localhost");
            return LOCALHOST_IPV4;
        }

        String ip;

        // 1. 检查 X-Forwarded-For 头部
        ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            // X-Forwarded-For 可能包含多个IP，取第一个
            if (ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }
            log.debug("Got IP from X-Forwarded-For: {}", ip);
            return ip;
        }

        // 2. 检查 Proxy-Client-IP 头部
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            log.debug("Got IP from Proxy-Client-IP: {}", ip);
            return ip;
        }

        // 3. 检查 WL-Proxy-Client-IP 头部 (WebLogic代理)
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            log.debug("Got IP from WL-Proxy-Client-IP: {}", ip);
            return ip;
        }

        // 4. 检查 HTTP_CLIENT_IP 头部
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            log.debug("Got IP from HTTP_CLIENT_IP: {}", ip);
            return ip;
        }

        // 5. 检查 HTTP_X_FORWARDED_FOR 头部
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            log.debug("Got IP from HTTP_X_FORWARDED_FOR: {}", ip);
            return ip;
        }

        // 6. 检查 X-Real-IP 头部 (Nginx常用)
        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            log.debug("Got IP from X-Real-IP: {}", ip);
            return ip;
        }

        // 7. 最终使用 request.getRemoteAddr()
        ip = request.getRemoteAddr();

        // 处理IPv6本地地址
        if (LOCALHOST_IPV6.equals(ip) || LOCALHOST_IPV6_SHORT.equals(ip)) {
            ip = LOCALHOST_IPV4;
            log.debug("Converted IPv6 localhost to IPv4: {}", ip);
        }

        log.debug("Got IP from request.getRemoteAddr(): {}", ip);
        return ip;
    }

    /**
     * 验证IP地址是否有效
     *
     * @param ip IP地址字符串
     * @return true表示IP地址有效，false表示无效
     */
    private static boolean isValidIp(String ip) {
        return StringUtils.hasText(ip) &&
                !UNKNOWN.equalsIgnoreCase(ip) &&
                !ip.startsWith("192.168.") && // 不使用内网IP作为主要IP
                !ip.startsWith("10.") &&
                !ip.startsWith("172.");
    }

    /**
     * 验证是否为合法的IPv4地址格式
     *
     * @param ip IP地址字符串
     * @return true表示格式正确，false表示格式错误
     */
    public static boolean isIPv4(String ip) {
        return StringUtils.hasText(ip) && IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 检查IP是否为内网地址
     *
     * @param ip IP地址字符串
     * @return true表示是内网地址，false表示是外网地址
     */
    public static boolean isInternalIp(String ip) {
        if (!StringUtils.hasText(ip)) {
            return false;
        }

        return ip.startsWith("192.168.") ||
                ip.startsWith("10.") ||
                ip.startsWith("172.") ||
                LOCALHOST_IPV4.equals(ip) ||
                LOCALHOST_IPV6.equals(ip) ||
                LOCALHOST_IPV6_SHORT.equals(ip);
    }

    /**
     * 获取IP地址的简要信息
     *
     * @param request HTTP请求对象
     * @return IP地址信息字符串，包含IP类型和地址
     */
    public static String getIpInfo(HttpServletRequest request) {
        String ip = getClientIpAddress(request);
        String type = isInternalIp(ip) ? "内网IP" : "外网IP";
        return String.format("%s: %s", type, ip);
    }
}