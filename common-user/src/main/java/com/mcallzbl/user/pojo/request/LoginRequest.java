package com.mcallzbl.user.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统一登录请求参数")
public class LoginRequest {

    @Email(message = "邮箱格式不正确")
    @NotNull(message = "邮箱不能为空")
    @Schema(description = "邮箱", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "密码（邮箱+密码登录时需要）", example = "password123")
    private String password;

    @Schema(description = "验证码（邮箱+验证码登录时需要）", example = "123456")
    private String verificationCode;

}