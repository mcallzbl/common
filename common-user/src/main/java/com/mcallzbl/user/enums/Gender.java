package com.mcallzbl.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 性别枚举
 *
 * @author mcallzbl
 * @since 2025-10-25
 */
@Getter
public enum Gender {

    /**
     * 未知
     */
    UNKNOWN(0, "未知"),

    /**
     * 男性
     */
    MALE(1, "男"),

    /**
     * 女性
     */
    FEMALE(2, "女");

    /**
     * code映射缓存
     */
    private static final Map<Integer, Gender> CODE_MAP;
    /**
     * description映射缓存
     */
    private static final Map<String, Gender> DESCRIPTION_MAP;

    static {
        Map<Integer, Gender> codeMap = new HashMap<>();
        Map<String, Gender> descriptionMap = new HashMap<>();

        for (Gender gender : values()) {
            codeMap.put(gender.getCode(), gender);
            descriptionMap.put(gender.getDescription(), gender);
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

    Gender(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static Gender fromCode(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        return CODE_MAP.getOrDefault(code, UNKNOWN);
    }

    /**
     * 根据描述获取枚举
     */
    public static Gender fromDescription(String description) {
        if (description == null) {
            return UNKNOWN;
        }
        return DESCRIPTION_MAP.getOrDefault(description, UNKNOWN);
    }

    /**
     * 获取所有code映射（不可修改）
     */
    public static Map<Integer, Gender> getCodeMap() {
        return CODE_MAP;
    }

    /**
     * 获取所有description映射（不可修改）
     */
    public static Map<String, Gender> getDescriptionMap() {
        return DESCRIPTION_MAP;
    }
}