package com.mcallzbl.commonaliyunoss.service;

import com.aliyun.sts20150401.Client;
import com.aliyun.sts20150401.models.AssumeRoleRequest;
import com.aliyun.sts20150401.models.AssumeRoleResponse;
import com.aliyun.teaopenapi.models.Config;
import com.mcallzbl.common.BusinessException;
import com.mcallzbl.commonaliyunoss.OssUploadAuthResponse;
import com.mcallzbl.user.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 阿里云OSS服务
 * 提供OSS直传授权和STS临时凭证生成功能
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OssService {

    // 文件类型对应的目录
    private static final String IMAGE_DIR = "images";
    private static final String VIDEO_DIR = "videos";
    private static final String DOCUMENT_DIR = "documents";
    private static final String AUDIO_DIR = "audios";
    // 文件大小限制（字节）
    private static final long MAX_IMAGE_SIZE = 10L * 1024 * 1024;  // 10MB
    private static final long MAX_VIDEO_SIZE = 500L * 1024 * 1024; // 500MB
    private static final long MAX_DOCUMENT_SIZE = 50L * 1024 * 1024; // 50MB
    private static final long MAX_AUDIO_SIZE = 100L * 1024 * 1024;  // 100MB
    @Value("${aliyun.oss.accessKeyId:}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret:}")
    private String accessKeySecret;
    @Value("${aliyun.oss.endpoint:https://oss-cn-beijing.aliyuncs.com}")
    private String ossEndpoint;
    @Value("${aliyun.oss.bucket:common-bucket}")
    private String ossBucket;
    @Value("${aliyun.sts.roleArn:}")
    private String roleArn;
    @Value("${aliyun.sts.region:cn-beijing}")
    private String region;
    @Value("${aliyun.oss.uploadPath:uploads}")
    private String uploadPath;

    /**
     * 生成OSS直传授权信息
     *
     * @param fileType 文件类型
     * @param fileName 文件名
     * @return OSS直传授权响应
     */
    public OssUploadAuthResponse generateUploadAuth(String fileType, String fileName) {
        try {
            // 1. 验证参数
            validateFileType(fileType, fileName);

            // 2. 生成唯一的文件路径
            String ossKey = generateOssKey(fileType, fileName);
            String fileUrl = String.format("https://%s.%s/%s", ossBucket, ossEndpoint.substring(8), ossKey);

            // 3. 获取STS临时凭证
            AssumeRoleResponse stsResponse = getStsCredentials();

            // 4. 获取文件大小限制
            Long maxFileSize = getMaxFileSize(fileType);

            // 5. 构建响应
            return OssUploadAuthResponse.builder()
                    .accessKeyId(stsResponse.getBody().getCredentials().getAccessKeyId())
                    .accessKeySecret(stsResponse.getBody().getCredentials().getAccessKeySecret())
                    .securityToken(stsResponse.getBody().getCredentials().getSecurityToken())
                    .expiration(stsResponse.getBody().getCredentials().getExpiration())
                    .bucket(ossBucket)
                    .endpoint(ossEndpoint)
                    .ossKey(ossKey)
                    .fileName(UUID.randomUUID() + getFileExtension(fileName))
                    .uploadUrlPrefix(String.format("https://%s.%s/", ossBucket, ossEndpoint.substring(8)))
                    .fileUrl(fileUrl)
                    .sessionName("oss-upload-" + UserContext.getCurrentUserId())
                    .maxFileSize(maxFileSize)
                    .build();

        } catch (Exception e) {
            log.error("[OssService.generateUploadAuth] 生成OSS直传授权失败", e);
            throw new BusinessException("获取OSS上传授权失败");
        }
    }

    /**
     * 仅获取STS临时凭证
     *
     * @return STS临时凭证响应
     */
    public OssUploadAuthResponse generateStsToken() {
        try {
            AssumeRoleResponse stsResponse = getStsCredentials();

            return OssUploadAuthResponse.builder()
                    .accessKeyId(stsResponse.getBody().getCredentials().getAccessKeyId())
                    .accessKeySecret(stsResponse.getBody().getCredentials().getAccessKeySecret())
                    .securityToken(stsResponse.getBody().getCredentials().getSecurityToken())
                    .expiration(stsResponse.getBody().getCredentials().getExpiration())
                    .bucket(ossBucket)
                    .endpoint(ossEndpoint)
                    .sessionName("oss-token-" + UserContext.getCurrentUserId())
                    .build();

        } catch (Exception e) {
            log.error("[OssService.generateStsToken] 生成STS临时凭证失败", e);
            throw new BusinessException("获取STS临时凭证失败");
        }
    }

    /**
     * 获取STS临时凭证
     */
    private AssumeRoleResponse getStsCredentials() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setRegionId(region);

        Client client = new Client(config);

        AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest()
                .setRoleArn(roleArn)
                .setRoleSessionName("oss-upload-" + UserContext.getCurrentUserId())
                .setDurationSeconds(3600L); // 1小时有效期

        return client.assumeRole(assumeRoleRequest);
    }

    /**
     * 生成OSS对象键
     */
    private String generateOssKey(String fileType, String fileName) {
        LocalDateTime now = LocalDateTime.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileExt = getFileExtension(fileName);
        String uniqueId = UUID.randomUUID().toString();

        String typeDir = getTypeDirectory(fileType);

        return String.format("%s/%s/%s/%s%s", uploadPath, typeDir, datePath, uniqueId, fileExt);
    }

    /**
     * 根据文件类型获取目录
     */
    private String getTypeDirectory(String fileType) {
        return switch (fileType.toLowerCase()) {
            case "image" -> IMAGE_DIR;
            case "video" -> VIDEO_DIR;
            case "document" -> DOCUMENT_DIR;
            case "audio" -> AUDIO_DIR;
            default -> "others";
        };
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 验证文件类型
     */
    private void validateFileType(String fileType, String fileName) {
        if (fileType == null || fileType.trim().isEmpty()) {
            throw new BusinessException("文件类型不能为空");
        }

        // 验证文件类型是否支持
        switch (fileType.toLowerCase()) {
            case "image":
                if (fileName != null && !isImageFile(fileName)) {
                    throw new BusinessException("不支持的图片格式");
                }
                break;
            case "video":
                if (fileName != null && !isVideoFile(fileName)) {
                    throw new BusinessException("不支持的视频格式");
                }
                break;
            case "document":
                if (fileName != null && !isDocumentFile(fileName)) {
                    throw new BusinessException("不支持的文档格式");
                }
                break;
            case "audio":
                if (fileName != null && !isAudioFile(fileName)) {
                    throw new BusinessException("不支持的音频格式");
                }
                break;
            default:
                throw new BusinessException("不支持的文件类型");
        }
    }

    /**
     * 获取文件大小限制
     */
    private Long getMaxFileSize(String fileType) {
        return switch (fileType.toLowerCase()) {
            case "image" -> MAX_IMAGE_SIZE;
            case "video" -> MAX_VIDEO_SIZE;
            case "document" -> MAX_DOCUMENT_SIZE;
            case "audio" -> MAX_AUDIO_SIZE;
            default -> MAX_DOCUMENT_SIZE;
        };
    }

    private boolean isImageFile(String fileName) {
        String ext = getFileExtension(fileName).toLowerCase();
        return ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png") ||
                ext.equals(".gif") || ext.equals(".bmp") || ext.equals(".webp");
    }

    private boolean isVideoFile(String fileName) {
        String ext = getFileExtension(fileName).toLowerCase();
        return ext.equals(".mp4") || ext.equals(".avi") || ext.equals(".mov") ||
                ext.equals(".wmv") || ext.equals(".flv") || ext.equals(".mkv");
    }

    private boolean isDocumentFile(String fileName) {
        String ext = getFileExtension(fileName).toLowerCase();
        return ext.equals(".pdf") || ext.equals(".doc") || ext.equals(".docx") ||
                ext.equals(".xls") || ext.equals(".xlsx") || ext.equals(".ppt") || ext.equals(".pptx") || ext.equals(".txt");
    }

    private boolean isAudioFile(String fileName) {
        String ext = getFileExtension(fileName).toLowerCase();
        return ext.equals(".mp3") || ext.equals(".wav") || ext.equals(".flac") ||
                ext.equals(".aac") || ext.equals(".ogg") || ext.equals(".m4a");
    }
}