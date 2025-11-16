package com.mcallzbl.commonaliyunoss.controller;

import com.mcallzbl.common.Result;
import com.mcallzbl.common.annotation.ResponseWrapper;
import com.mcallzbl.commonaliyunoss.OssUploadAuthResponse;
import com.mcallzbl.commonaliyunoss.service.OssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 阿里云OSS文件上传控制器
 * 提供客户端直传OSS的授权接口
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/14
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oss")
@Tag(name = "OSS文件上传", description = "阿里云OSS文件直传授权接口")
public class OssController {

    private final OssService ossService;

    /**
     * 获取OSS直传授权信息
     * 移动端客户端调用此接口获取STS临时凭证，然后直接上传文件到OSS
     *
     * @param fileType 文件类型 (image, video, document, audio)
     * @param fileName 文件名（可选，用于验证文件类型）
     * @return OSS直传授权信息，包含STS临时凭证和上传参数
     */
    @Operation(
            summary = "获取OSS直传授权",
            description = "获取STS临时凭证和OSS上传参数，用于客户端直接上传文件到OSS"
    )
    @ResponseWrapper
    @GetMapping("/upload-auth")
    public Result<OssUploadAuthResponse> getUploadAuth(
            @Parameter(description = "文件类型", example = "image")
            @RequestParam String fileType,
            @Parameter(description = "文件名", example = "avatar.jpg")
            @RequestParam(required = false) String fileName) {

        log.debug("[OssController.getUploadAuth] params: fileType={}, fileName={}", fileType, fileName);

        OssUploadAuthResponse authResponse = ossService.generateUploadAuth(fileType, fileName);

        log.info("[OssController.getUploadAuth] OSS直传授权生成成功: fileType={}, ossKey={}",
                fileType, authResponse.getOssKey());

        return Result.success(authResponse);
    }

    /**
     * 获取STS临时凭证（备用接口）
     * 当客户端需要自行处理上传逻辑时使用
     *
     * @return STS临时凭证信息
     */
    @Operation(
            summary = "获取STS临时凭证",
            description = "仅获取STS临时凭证，用于客户端自行处理OSS上传逻辑"
    )
    @ResponseWrapper
    @GetMapping("/sts-token")
    public Result<OssUploadAuthResponse> getStsToken() {
        log.debug("[OssController.getStsToken] 获取STS临时凭证");

        OssUploadAuthResponse stsToken = ossService.generateStsToken();

        log.info("[OssController.getStsToken] STS临时凭证生成成功");

        return Result.success(stsToken);
    }
}