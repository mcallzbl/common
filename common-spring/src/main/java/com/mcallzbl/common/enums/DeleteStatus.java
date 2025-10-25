package com.mcallzbl.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 删除状态枚举
 *
 * @author mcallzbl
 * @since 2025-10-25
 */
@Getter
public enum DeleteStatus {

    /**
     * 正常（未删除）
     */
    NORMAL(0, "正常"),

    /**
     * 已删除
     */
    DELETED(1, "已删除");

    /**
     * code映射缓存
     */
    private static final Map<Integer, DeleteStatus> CODE_MAP;
    /**
     * description映射缓存
     */
    private static final Map<String, DeleteStatus> DESCRIPTION_MAP;

    static {
        Map<Integer, DeleteStatus> codeMap = new HashMap<>();
        Map<String, DeleteStatus> descriptionMap = new HashMap<>();

        for (DeleteStatus status : values()) {
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

    DeleteStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static DeleteStatus fromCode(Integer code) {
        if (code == null) {
            return NORMAL;
        }
        return CODE_MAP.getOrDefault(code, NORMAL);
    }

    /**
     * 根据描述获取枚举
     */
    public static DeleteStatus fromDescription(String description) {
        if (description == null) {
            return NORMAL;
        }
        return DESCRIPTION_MAP.getOrDefault(description, NORMAL);
    }

    /**
     * 获取所有code映射（不可修改）
     */
    public static Map<Integer, DeleteStatus> getCodeMap() {
        return CODE_MAP;
    }

    /**
     * 获取所有description映射（不可修改）
     */
    public static Map<String, DeleteStatus> getDescriptionMap() {
        return DESCRIPTION_MAP;
    }

    /**
     * 检查是否已删除
     */
    public boolean isDeleted() {
        return this == DELETED;
    }

    /**
     * 检查是否正常（未删除）
     */
    public boolean isNormal() {
        return this == NORMAL;
    }

    /**
     * 检查是否可以访问（只有正常状态可以访问）
     */
    public boolean canAccess() {
        return this == NORMAL;
    }
}
