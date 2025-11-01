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
@Schema(description = "邮箱验证响应")
public class VerificationEmailResponse {

    @Schema(description = "邮箱地址", example = "user@example.com")
    private String email;

    @Schema(description = "过期时间戳（毫秒）", example = "1735689599000")
    private Long expireTime;
}
