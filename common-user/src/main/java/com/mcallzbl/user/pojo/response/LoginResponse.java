package com.mcallzbl.user.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "登录响应数据")
public class LoginResponse {

    @Schema(description = "昵称", example = "小明")
    private String nickname;

    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "访问令牌（Access Token）", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "刷新令牌（Refresh Token，Web端为null，移动端返回）", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "访问令牌过期时间（秒）", example = "3600")
    private Long accessTokenExpiresIn;

    @Schema(description = "刷新令牌过期时间（秒）", example = "604800")
    private Long refreshTokenExpiresIn;
}