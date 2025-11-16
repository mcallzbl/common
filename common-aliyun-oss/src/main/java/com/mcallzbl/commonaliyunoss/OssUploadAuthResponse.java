package com.mcallzbl.commonaliyunoss;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OSS直传授权响应DTO
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "OSS直传授权响应")
public class OssUploadAuthResponse {

    @Schema(description = "OSS AccessKeyId", example = "LTAI5t...")
    private String accessKeyId;

    @Schema(description = "OSS AccessKeySecret", example = "7rPxK...")
    private String accessKeySecret;

    @Schema(description = "STS SecurityToken", example = "CAIS...")
    private String securityToken;

    @Schema(description = "凭证过期时间", example = "2025-11-14T15:30:00Z")
    private String expiration;

    @Schema(description = "OSS Bucket名称", example = "icongyou-bucket")
    private String bucket;

    @Schema(description = "OSS Endpoint", example = "https://oss-cn-hangzhou.aliyuncs.com")
    private String endpoint;

    @Schema(description = "OSS对象键（文件路径）", example = "uploads/avatar/2025/11/14/abc123.jpg")
    private String ossKey;

    @Schema(description = "上传文件名", example = "abc123.jpg")
    private String fileName;

    @Schema(description = "文件上传URL前缀", example = "https://icongyou-bucket.oss-cn-hangzhou.aliyuncs.com/")
    private String uploadUrlPrefix;

    @Schema(description = "文件访问URL（上传完成后）", example = "https://icongyou-bucket.oss-cn-hangzhou.aliyuncs.com/uploads/avatar/2025/11/14/abc123.jpg")
    private String fileUrl;

    @Schema(description = "STS会话名称", example = "oss-client-upload")
    private String sessionName;

    @Schema(description = "文件大小限制（字节）", example = "52428800")
    private Long maxFileSize;
}