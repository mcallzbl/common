package com.mcallzbl.common.dto;

import com.mcallzbl.common.Result;
import com.mcallzbl.common.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 通用结果传输DTO
 * 用于最终的API响应传输
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通用结果传输对象")
public class CommonResultTransferVO<T> {

    /**
     * 业务状态码
     */
    @Schema(description = "业务状态码", example = "200")
    private int code;

    /**
     * 响应消息
     */
    @Schema(description = "响应消息", example = "操作成功")
    private String message;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    /**
     * 时间戳
     */
    @Schema(description = "时间戳", example = "2025-11-01T11:00:42.798042945Z")
    private Instant timestamp;

    /**
     * 从CommonResult创建传输DTO
     */
    public static <T> CommonResultTransferVO<T> from(Result<T> result) {
        return CommonResultTransferVO.<T>builder()
                .code(result.getCode())
                .message(result.getMessage())
                .data(result.getData())
                .timestamp(result.getTimestamp())
                .build();
    }

    /**
     * 创建成功传输DTO
     */
    public static <T> CommonResultTransferVO<T> success(T data) {
        return CommonResultTransferVO.<T>builder()
                .code(200)
                .message(ResultCode.SUCCESS.getMessage())
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * 创建成功传输DTO（带自定义消息）
     */
    public static <T> CommonResultTransferVO<T> success(T data, String message) {
        return CommonResultTransferVO.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * 创建失败传输DTO
     */
    public static <T> CommonResultTransferVO<T> failed(int code, String message) {
        return CommonResultTransferVO.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * 创建失败传输DTO（带数据）
     */
    public static <T> CommonResultTransferVO<T> failed(int code, String message, T data) {
        return CommonResultTransferVO.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }
}