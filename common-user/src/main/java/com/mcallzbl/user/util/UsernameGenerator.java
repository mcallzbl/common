package com.mcallzbl.user.util;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用户名生成工具类
 * 提供多种用户名生成策略
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
@Slf4j
public class UsernameGenerator {

    // 用户名字符池：字母、数字、下划线
    private static final String USERNAME_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
    private static final int DEFAULT_USERNAME_LENGTH = 8;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 生成随机用户名
     * 支持字母、数字、下划线组合
     *
     * @return 生成的随机用户名
     */
    public static String generateRandomUsername() {
        return generateRandomUsername(DEFAULT_USERNAME_LENGTH);
    }

    /**
     * 生成指定长度的随机用户名
     * 支持字母、数字、下划线组合
     *
     * @param length 用户名长度
     * @return 生成的随机用户名
     */
    public static String generateRandomUsername(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("用户名长度必须大于0");
        }

        // 确保用户名不以数字或下划线开头，以字母开头
        StringBuilder username = new StringBuilder();

        // 第一个字符：字母
        username.append(USERNAME_CHARS.charAt(SECURE_RANDOM.nextInt(52))); // 52个字母

        // 后续字符：字母、数字、下划线
        for (int i = 1; i < length; i++) {
            username.append(USERNAME_CHARS.charAt(SECURE_RANDOM.nextInt(USERNAME_CHARS.length())));
        }

        String generatedUsername = username.toString();
        log.debug("生成随机用户名: {}", generatedUsername);
        return generatedUsername;
    }

    /**
     * 生成带前缀的随机用户名
     * 格式：前缀 + 时间戳后缀 + 随机字符
     *
     * @param prefix 用户名前缀
     * @return 带前缀的随机用户名
     */
    public static String generateRandomUsernameWithPrefix(String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        String randomSuffix = generateRandomUsername(4);
        return prefix + "_" + timestamp + "_" + randomSuffix;
    }

    /**
     * 生成基于邮箱的默认用户名
     * 取邮箱@符号前的部分，如果太长则截取并添加随机后缀
     *
     * @param email 邮箱地址
     * @return 基于邮箱的用户名
     */
    public static String generateUsernameFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return generateRandomUsername();
        }

        String emailUsername = email.split("@")[0];

        // 清理邮箱用户名：移除特殊字符，只保留字母数字下划线
        String cleanedUsername = emailUsername.replaceAll("[^a-zA-Z0-9_]", "_");

        // 限制长度
        if (cleanedUsername.length() > 16) {
            cleanedUsername = cleanedUsername.substring(0, 16);
        }

        // 确保不以数字开头
        if (cleanedUsername.length() > 0 && Character.isDigit(cleanedUsername.charAt(0))) {
            cleanedUsername = "user_" + cleanedUsername;
        }

        // 如果清理后为空，生成随机用户名
        if (cleanedUsername.isEmpty()) {
            return generateRandomUsername();
        }

        return cleanedUsername;
    }

    /**
     * 生成唯一用户名（带递增后缀）
     * 如果基础用户名已存在，自动添加数字后缀
     *
     * @param baseUsername 基础用户名
     * @return 唯一的用户名
     */
    public static String generateUniqueUsername(String baseUsername) {
        if (baseUsername == null || baseUsername.trim().isEmpty()) {
            return generateRandomUsername();
        }

        return baseUsername;
    }
}