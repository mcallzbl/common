package com.mcallzbl.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户状态枚举
 *
 * @author mcallzbl
 * @since 2025-10-25
 */
@Getter
public enum UserStatus {

    /**
     * 禁用
     */
    DISABLED(0, "禁用"),

    /**
     * 正常
     */
    NORMAL(1, "正常"),

    /**
     * 冻结
     */
    FROZEN(2, "冻结");

    /**
     * code映射缓存
     */
    private static final Map<Integer, UserStatus> CODE_MAP;
    /**
     * description映射缓存
     */
    private static final Map<String, UserStatus> DESCRIPTION_MAP;

    static {
        Map<Integer, UserStatus> codeMap = new HashMap<>();
        Map<String, UserStatus> descriptionMap = new HashMap<>();

        for (UserStatus status : values()) {
            codeMap.put(status.getCode(), status);
            descriptionMap.put(status.getDescription(), status);
        }

        CODE_MAP = Collections.unmodifiableMap(codeMap);
        DESCRIPTION_MAP = Collections.unmodifiableMap(descriptionMap);
    }

    /**
     * 存储在数据库中的值
     */
    @EnumValue
    private final int code;
    /**
     * 描述
     */
    private final String description;

    UserStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static UserStatus fromCode(Integer code) {
        if (code == null) {
            return DISABLED;
        }
        return CODE_MAP.getOrDefault(code, DISABLED);
    }

    /**
     * 根据描述获取枚举
     */
    public static UserStatus fromDescription(String description) {
        if (description == null) {
            return DISABLED;
        }
        return DESCRIPTION_MAP.getOrDefault(description, DISABLED);
    }

    /**
     * 获取所有code映射（不可修改）
     */
    public static Map<Integer, UserStatus> getCodeMap() {
        return CODE_MAP;
    }

    /**
     * 获取所有description映射（不可修改）
     */
    public static Map<String, UserStatus> getDescriptionMap() {
        return DESCRIPTION_MAP;
    }

    /**
     * 检查是否为正常状态
     */
    public boolean isNormal() {
        return this == NORMAL;
    }

    /**
     * 检查是否为禁用状态
     */
    public boolean isDisabled() {
        return this == DISABLED;
    }

    /**
     * 检查是否为冻结状态
     */
    public boolean isFrozen() {
        return this == FROZEN;
    }

    /**
     * 检查是否可以登录（只有正常状态可以登录）
     */
    public boolean canLogin() {
        return this == NORMAL;
    }
}
