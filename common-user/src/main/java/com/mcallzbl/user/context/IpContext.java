package com.mcallzbl.user.context;

import lombok.extern.slf4j.Slf4j;

/**
 * IP地址上下文
 * 使用ThreadLocal存储当前请求线程的IP地址信息
 *
 * @author mcallzbl
 * @since 2025-11-01
 */
@Slf4j
public class IpContext {

    private static final ThreadLocal<String> IP_HOLDER = new ThreadLocal<>();

    /**
     * 设置当前请求的IP地址
     *
     * @param ip IP地址字符串
     */
    public static void setIp(String ip) {
        log.debug("设置当前请求IP地址: {}", ip);
        IP_HOLDER.set(ip);
    }

    /**
     * 获取当前请求的IP地址
     *
     * @return IP地址字符串，如果未设置则返回null
     */
    public static String getIp() {
        String ip = IP_HOLDER.get();
        log.debug("获取当前请求IP地址: {}", ip);
        return ip;
    }

    /**
     * 获取当前请求的IP地址，如果为空则返回默认值
     *
     * @param defaultIp 默认IP地址
     * @return IP地址字符串
     */
    public static String getIpOrDefault(String defaultIp) {
        String ip = getIp();
        return ip != null ? ip : defaultIp;
    }

    /**
     * 检查当前请求是否有IP地址
     *
     * @return true表示有IP地址，false表示没有
     */
    public static boolean hasIp() {
        return getIp() != null;
    }

    /**
     * 清除当前线程的IP地址
     * 请求完成后必须调用，防止ThreadLocal内存泄漏
     */
    public static void clear() {
        log.debug("清除当前请求IP地址");
        IP_HOLDER.remove();
    }

    /**
     * 获取当前线程的IP地址并清除
     * 用于请求完成时的清理
     *
     * @return IP地址字符串
     */
    public static String getIpAndClear() {
        String ip = getIp();
        clear();
        return ip;
    }
}