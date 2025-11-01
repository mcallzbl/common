package com.mcallzbl.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // 通用状态码
    SUCCESS(200, "操作成功", "common.success"),
    FAILED(500, "操作失败", "common.failed"),

    // 参数校验相关
    VALIDATION_FAILED(400, "参数校验失败", "common.validation.failed"),

    // 认证授权相关
    UNAUTHORIZED(401, "未授权或token已过期", "common.unauthorized"),
    FORBIDDEN(403, "无相关权限", "common.forbidden"),
    NOT_FOUND(404, "资源不存在", "common.not.found"),

    // 用户相关错误码 1001+
    USER_NOT_FOUND(1001, "用户不存在", "user.not.found"),
    USER_ALREADY_EXISTS(1002, "用户已存在", "user.already.exists"),
    PASSWORD_INCORRECT(1003, "密码错误", "user.password.incorrect"),
    USER_DISABLED(1004, "用户已被禁用", "user.disabled"),
    USER_LOCKED(1005, "用户已被锁定", "user.locked"),

    // 验证码相关错误码 2001+
    CAPTCHA_INVALID(2001, "验证码错误", "captcha.invalid"),
    CAPTCHA_EXPIRED(2002, "验证码已过期", "captcha.expired"),
    CAPTCHA_NOT_EXISTS(2003, "验证码不存在", "captcha.not.exists"),
    CAPTCHA_SEND_FAILED(2004, "验证码发送失败", "captcha.send.failed"),
    CAPTCHA_SEND_TOO_FREQUENT(2005, "验证码发送过于频繁", "captcha.send.too.frequent"),
    EMAIL_VERIFICATION_CODE_ERROR(2006, "邮箱验证码错误或已过期", "captcha.email.verification.error"),

    // Token相关错误码 3001+
    TOKEN_INVALID(3001, "token无效", "token.invalid"),
    TOKEN_EXPIRED(3002, "token已过期", "token.expired"),
    TOKEN_NOT_EXISTS(3003, "token不存在", "token.not.exists"),
    TOKEN_REFRESH_FAILED(3004, "token刷新失败", "token.refresh.failed"),

    // 功能相关错误码 4001+
    OPERATION_NOT_SUPPORTED(4001, "不支持的操作", "operation.not.supported"),
    RESOURCE_CONFLICT(4002, "资源冲突", "resource.conflict"),
    RATE_LIMIT_EXCEEDED(4003, "请求频率超限", "rate.limit.exceeded"),
    SYSTEM_BUSY(4004, "系统繁忙，请稍后重试", "system.busy"),
    INSUFFICIENT_PERMISSIONS(4005, "权限不足", "insufficient.permissions"),

    // 文件相关错误码 5001+
    FILE_NOT_FOUND(5001, "文件不存在", "file.not.found"),
    FILE_UPLOAD_FAILED(5002, "文件上传失败", "file.upload.failed"),
    FILE_SIZE_EXCEEDED(5003, "文件大小超出限制", "file.size.exceeded"),
    FILE_TYPE_NOT_SUPPORTED(5004, "文件类型不支持", "file.type.not.supported"),

    // 业务相关错误码 6001+
    BUSINESS_ERROR(6001, "业务处理失败", "business.error"),
    DATA_INCONSISTENT(6002, "数据不一致", "data.inconsistent"),
    OPERATION_TIMEOUT(6003, "操作超时", "operation.timeout"),
    INSUFFICIENT_BALANCE(6004, "余额不足", "insufficient.balance"),
    ORDER_NOT_FOUND(6005, "订单不存在", "order.not.found"),
    ORDER_STATUS_ERROR(6006, "订单状态错误", "order.status.error");

    private final int code;
    private final String message;
    private final String messageKey;
}