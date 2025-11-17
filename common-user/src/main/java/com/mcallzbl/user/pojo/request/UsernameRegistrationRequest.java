package com.mcallzbl.user.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户名注册请求参数
 * 支持用户名+邮箱+密码的注册方式
 *
 * @author mcallzbl
 * @since 2025-11-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户名注册请求参数")
public class UsernameRegistrationRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "用户名只能包含字母、数字、下划线和连字符")
    @Schema(description = "用户名", example = "john_doe")
    private String username;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱地址", example = "user@example.com")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 128, message = "密码长度必须在6-128个字符之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "密码必须包含至少一个小写字母、一个大写字母和一个数字")
    @Schema(description = "密码", example = "Password123")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码", example = "Password123")
    private String confirmPassword;

    @Size(max = 100, message = "昵称长度不能超过100个字符")
    @Schema(description = "用户昵称（可选）", example = "John Doe")
    private String nickname;

    /**
     * 验证两次密码是否一致
     *
     * @return true: 密码一致, false: 密码不一致
     */
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}