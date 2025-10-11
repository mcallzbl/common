package com.mcallzbl.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * 业务异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    /**
     * HTTP状态码
     */
    private HttpStatus httpStatus;

    /**
     * 业务错误码
     */
    private int code;

    /**
     * 用户友好的错误消息
     */
    private String userFriendlyMessage;

    /**
     * 附加数据
     */
    private Object data;

    public BusinessException(HttpStatus httpStatus, int code, String userFriendlyMessage) {
        super(userFriendlyMessage);
        this.httpStatus = httpStatus;
        this.code = code;
        this.userFriendlyMessage = userFriendlyMessage;
    }

    public BusinessException(HttpStatus httpStatus, int code, String userFriendlyMessage, Object data) {
        super(userFriendlyMessage);
        this.httpStatus = httpStatus;
        this.code = code;
        this.userFriendlyMessage = userFriendlyMessage;
        this.data = data;
    }

    public BusinessException(HttpStatus httpStatus, ResultCode resultCode) {
        super(resultCode.getMessage());
        this.httpStatus = httpStatus;
        this.code = resultCode.getCode();
        this.userFriendlyMessage = resultCode.getMessage();
    }

    public BusinessException(HttpStatus httpStatus, ResultCode resultCode, Object data) {
        super(resultCode.getMessage());
        this.httpStatus = httpStatus;
        this.code = resultCode.getCode();
        this.userFriendlyMessage = resultCode.getMessage();
        this.data = data;
    }

    public BusinessException(ResultCode resultCode) {
        this(HttpStatus.OK, resultCode);
    }

    public BusinessException(ResultCode resultCode, Object data) {
        this(HttpStatus.OK, resultCode, data);
    }

    /**
     * 创建参数校验失败异常
     */
    public static BusinessException validationFailed(String message) {
        return new BusinessException(HttpStatus.BAD_REQUEST, ResultCode.VALIDATION_FAILED.getCode(), message);
    }

    /**
     * 创建未授权异常
     */
    public static BusinessException unauthorized(String message) {
        return new BusinessException(HttpStatus.UNAUTHORIZED, ResultCode.UNAUTHORIZED.getCode(), message);
    }

    /**
     * 创建权限不足异常
     */
    public static BusinessException forbidden(String message) {
        return new BusinessException(HttpStatus.FORBIDDEN, ResultCode.FORBIDDEN.getCode(), message);
    }

    /**
     * 创建资源不存在异常
     */
    public static BusinessException notFound(String message) {
        return new BusinessException(HttpStatus.NOT_FOUND, ResultCode.NOT_FOUND.getCode(), message);
    }

    /**
     * 创建用户不存在异常
     */
    public static BusinessException userNotFound(String message) {
        return new BusinessException(HttpStatus.OK, ResultCode.USER_NOT_FOUND.getCode(), message);
    }

    /**
     * 创建用户已存在异常
     */
    public static BusinessException userAlreadyExists(String message) {
        return new BusinessException(HttpStatus.OK, ResultCode.USER_ALREADY_EXISTS.getCode(), message);
    }

    /**
     * 创建密码错误异常
     */
    public static BusinessException passwordIncorrect(String message) {
        return new BusinessException(HttpStatus.OK, ResultCode.PASSWORD_INCORRECT.getCode(), message);
    }

    /**
     * 创建验证码错误异常
     */
    public static BusinessException captchaInvalid(String message) {
        return new BusinessException(HttpStatus.OK, ResultCode.CAPTCHA_INVALID.getCode(), message);
    }

    /**
     * 创建验证码过期异常
     */
    public static BusinessException captchaExpired(String message) {
        return new BusinessException(HttpStatus.OK, ResultCode.CAPTCHA_EXPIRED.getCode(), message);
    }

    /**
     * 创建token无效异常
     */
    public static BusinessException tokenInvalid(String message) {
        return new BusinessException(HttpStatus.UNAUTHORIZED, ResultCode.TOKEN_INVALID.getCode(), message);
    }

    /**
     * 创建token过期异常
     */
    public static BusinessException tokenExpired(String message) {
        return new BusinessException(HttpStatus.UNAUTHORIZED, ResultCode.TOKEN_EXPIRED.getCode(), message);
    }

    /**
     * 创建业务错误异常
     */
    public static BusinessException businessError(String message) {
        return new BusinessException(HttpStatus.OK, ResultCode.BUSINESS_ERROR.getCode(), message);
    }

    /**
     * 创建系统繁忙异常
     */
    public static BusinessException systemBusy(String message) {
        return new BusinessException(HttpStatus.SERVICE_UNAVAILABLE, ResultCode.SYSTEM_BUSY.getCode(), message);
    }

    /**
     * 创建频率限制异常
     */
    public static BusinessException rateLimitExceeded(String message) {
        return new BusinessException(HttpStatus.TOO_MANY_REQUESTS, ResultCode.RATE_LIMIT_EXCEEDED.getCode(), message);
    }

    // ==================== of 静态工厂方法 ====================

    /**
     * 创建业务异常（HTTP状态码 + 业务错误码）
     */
    public static BusinessException of(HttpStatus httpStatus, ResultCode resultCode) {
        return new BusinessException(httpStatus, resultCode);
    }

    /**
     * 创建业务异常（HTTP状态码 + 业务错误码 + 数据）
     */
    public static BusinessException of(HttpStatus httpStatus, ResultCode resultCode, Object data) {
        return new BusinessException(httpStatus, resultCode, data);
    }

    /**
     * 创建业务异常（HTTP状态码 + 错误码 + 消息）
     */
    public static BusinessException of(HttpStatus httpStatus, int code, String message) {
        return new BusinessException(httpStatus, code, message);
    }

    /**
     * 创建业务异常（HTTP状态码 + 错误码 + 消息 + 数据）
     */
    public static BusinessException of(HttpStatus httpStatus, int code, String message, Object data) {
        return new BusinessException(httpStatus, code, message, data);
    }

    /**
     * 创建业务异常（业务错误码）
     */
    public static BusinessException of(ResultCode resultCode) {
        return new BusinessException(resultCode);
    }

    /**
     * 创建业务异常（业务错误码 + 数据）
     */
    public static BusinessException of(ResultCode resultCode, Object data) {
        return new BusinessException(resultCode, data);
    }

    /**
     * 创建业务异常（业务错误码 + 自定义消息）
     */
    public static BusinessException of(ResultCode resultCode, String message) {
        return new BusinessException(HttpStatus.OK, resultCode.getCode(), message);
    }

    /**
     * 创建业务异常（业务错误码 + 自定义消息 + 数据）
     */
    public static BusinessException of(ResultCode resultCode, String message, Object data) {
        return new BusinessException(HttpStatus.OK, resultCode.getCode(), message, data);
    }

    /**
     * 创建业务异常（仅自定义消息，使用默认失败状态码）
     */
    public static BusinessException of(String message) {
        return new BusinessException(HttpStatus.OK, ResultCode.FAILED.getCode(), message);
    }

    /**
     * 创建业务异常（自定义消息 + 数据，使用默认失败状态码）
     */
    public static BusinessException of(String message, Object data) {
        return new BusinessException(HttpStatus.OK, ResultCode.FAILED.getCode(), message, data);
    }
}