package com.mcallzbl.user.pojo.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mcallzbl.common.BusinessException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/7/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationEmailRequest {
    @Schema(description = "目标邮箱", example = "xxx@qq.com")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "验证目的", example = "login")
    @NotNull(message = "验证目的不能为空")
    private Purpose purpose;


    public enum Purpose {
        RESET_PASSWORD("reset_password"),
        CHANGE_EMAIL("change_email"),
        LOGIN("login");

        /**
         * value映射缓存
         */
        private static final Map<String, Purpose> VALUE_MAP;

        static {
            Map<String, Purpose> valueMap = new HashMap<>();
            for (Purpose purpose : values()) {
                valueMap.put(purpose.getValue().toLowerCase(), purpose);
            }
            VALUE_MAP = Collections.unmodifiableMap(valueMap);
        }

        private final String value;

        Purpose(String value) {
            this.value = value;
        }

        @JsonCreator
        public static Purpose fromValue(String value) {
            if (value == null) {
                throw new BusinessException("无效的邮箱验证目的: null");
            }
            Purpose purpose = VALUE_MAP.get(value.toLowerCase());
            if (purpose == null) {
                throw new BusinessException("无效的邮箱验证目的: " + value);
            }
            return purpose;
        }

        /**
         * 根据value获取枚举（不抛异常版本）
         */
        public static Purpose fromValueOrNull(String value) {
            if (value == null) {
                return null;
            }
            return VALUE_MAP.get(value.toLowerCase());
        }

        /**
         * 获取所有value映射（不可修改）
         */
        public static Map<String, Purpose> getValueMap() {
            return VALUE_MAP;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
