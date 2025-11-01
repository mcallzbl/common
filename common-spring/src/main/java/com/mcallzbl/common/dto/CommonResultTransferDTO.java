package com.mcallzbl.common.dto;

import com.mcallzbl.common.Result;
import com.mcallzbl.common.ResultCode;
import com.mcallzbl.common.util.CommonI18nUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.time.Instant;

/**
 * 通用结果传输DTO
 * 用于最终的API响应传输
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResultTransferDTO<T> {

    /**
     * 业务状态码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Instant timestamp;

    /**
     * 从CommonResult创建传输DTO
     */
    public static <T> CommonResultTransferDTO<T> from(Result<T> result) {
        return CommonResultTransferDTO.<T>builder()
                .code(result.getCode())
                .message(result.getMessage())
                .data(result.getData())
                .timestamp(result.getTimestamp())
                .build();
    }

    /**
     * 创建成功传输DTO
     */
    public static <T> CommonResultTransferDTO<T> success(T data) {
        return CommonResultTransferDTO.<T>builder()
                .code(200)
                .message(ResultCode.SUCCESS.getMessage())
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * 创建成功传输DTO（带自定义消息）
     */
    public static <T> CommonResultTransferDTO<T> success(T data, String message) {
        return CommonResultTransferDTO.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * 创建失败传输DTO
     */
    public static <T> CommonResultTransferDTO<T> failed(int code, String message) {
        return CommonResultTransferDTO.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * 创建失败传输DTO（带数据）
     */
    public static <T> CommonResultTransferDTO<T> failed(int code, String message, T data) {
        return CommonResultTransferDTO.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }
}