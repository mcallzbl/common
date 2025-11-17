package com.mcallzbl.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 角色状态枚举
 *
 * @author mcallzbl
 * @since 2025-11-17
 */
@Getter
public enum RoleStatus {

    /**
     * 禁用
     */
    DISABLED(0, "禁用"),

    /**
     * 启用
     */
    ENABLED(1, "启用");

    @EnumValue
    private final Integer code;

    private final String description;

    private static final Map<String, RoleStatus> NAME_MAP = new ConcurrentHashMap<>();

    static {
        for (RoleStatus status : values()) {
            NAME_MAP.put(status.name().toLowerCase(), status);
        }
    }

    RoleStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据字符串转换为枚举值（不区分大小写）
     */
    @JsonCreator
    public static RoleStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        return NAME_MAP.get(value.toLowerCase().trim());
    }

    /**
     * JSON序列化时返回枚举名称
     */
    @JsonValue
    public String getName() {
        return this.name();
    }

    /**
     * 根据代码获取枚举
     */
    public static RoleStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (RoleStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}