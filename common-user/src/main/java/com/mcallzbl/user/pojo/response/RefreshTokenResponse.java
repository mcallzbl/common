package com.mcallzbl.user.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "刷新Token响应数据")
public class RefreshTokenResponse {

    @Schema(description = "新的访问令牌（Access Token）", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "新的刷新令牌（Refresh Token，Web端为null，移动端返回）", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "访问令牌过期时间（秒）", example = "3600")
    private Long accessTokenExpiresIn;

    @Schema(description = "刷新令牌过期时间（秒）", example = "604800")
    private Long refreshTokenExpiresIn;
}