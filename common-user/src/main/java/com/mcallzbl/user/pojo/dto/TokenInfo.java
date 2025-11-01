package com.mcallzbl.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Token信息DTO
 * 包含token字符串和过期时间
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {

    /**
     * Token字符串
     */
    private String token;

    /**
     * 过期时间（时间戳）
     */
    private long expiration;

    /**
     * 过期时间（秒）
     */
    private long expiresIn;

    /**
     * Token类型
     */
    private String type;

    /**
     * 获取过期时间的LocalDateTime表示
     */
    public LocalDateTime getExpirationTime() {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(expiration),
                java.time.ZoneId.systemDefault()
        );
    }

    /**
     * 判断token是否已过期
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expiration;
    }
}