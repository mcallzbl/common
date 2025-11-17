package com.mcallzbl.user.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
@Schema(description = "邮箱登录请求参数")
public class EmailLoginRequest {

    @Email(message = "邮箱格式不正确")
    @NotNull(message = "邮箱不能为空")
    @Schema(description = "邮箱", example = "user@example.com")
    private String email;

    @Size(min = 6, message = "密码长度不能少于6位")
    @Schema(description = "密码（邮箱+密码登录时需要）", example = "123456")
    private String password;

    @Size(min = 6, max = 6, message = "验证码长度必须为6位")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须为6位数字")
    @Schema(description = "验证码（邮箱+验证码登录时需要）", example = "123456")
    private String verificationCode;

}