package com.mcallzbl.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 统一响应结果包装类
 */
@Data
public class Result<T> {
    private int httpStatus;
    private int code;
    private String message;
    private T data;
//    private long timestamp;

    public Result() {
//        this.timestamp = System.currentTimeMillis();
        this.httpStatus = HttpStatus.OK.value();
    }

    private Result(int httpStatus, int code, String message, T data) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.data = data;
//        this.timestamp = System.currentTimeMillis();
    }

    // ==================== 成功返回方法 ====================

    /**
     * 成功返回结果（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(),
                data
        );
    }

    /**
     * 成功返回结果（带数据和自定义消息）
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.SUCCESS.getCode(),
                message,
                data
        );
    }

    /**
     * 成功返回结果（仅消息，无数据）
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.SUCCESS.getCode(),
                message,
                null
        );
    }

    /**
     * 成功返回结果（默认成功消息，无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(),
                null
        );
    }

    /**
     * 成功返回结果（自定义HTTP状态码）
     */
    public static <T> Result<T> success(HttpStatus httpStatus, T data) {
        return new Result<>(
                httpStatus.value(),
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(),
                data
        );
    }

    /**
     * 成功返回结果（自定义HTTP状态码和消息）
     */
    public static <T> Result<T> success(HttpStatus httpStatus, T data, String message) {
        return new Result<>(
                httpStatus.value(),
                ResultCode.SUCCESS.getCode(),
                message,
                data
        );
    }

    // ==================== 失败返回方法 ====================

    /**
     * 失败返回结果（指定HTTP状态码和错误码）
     */
    public static <T> Result<T> failed(HttpStatus httpStatus, ResultCode errorCode) {
        return new Result<>(
                httpStatus.value(),
                errorCode.getCode(),
                errorCode.getMessage(),
                null
        );
    }

    /**
     * 失败返回结果（指定HTTP状态码和自定义消息）
     */
    public static <T> Result<T> failed(HttpStatus httpStatus, String message) {
        return new Result<>(
                httpStatus.value(),
                ResultCode.FAILED.getCode(),
                message,
                null
        );
    }

    /**
     * 失败返回结果（指定错误码）
     */
    public static <T> Result<T> failed(ResultCode errorCode) {
        return new Result<>(
                HttpStatus.OK.value(),
                errorCode.getCode(),
                errorCode.getMessage(),
                null
        );
    }

    /**
     * 失败返回结果（指定错误码和数据）
     */
    public static <T> Result<T> failed(ResultCode errorCode, T data) {
        return new Result<>(
                HttpStatus.OK.value(),
                errorCode.getCode(),
                errorCode.getMessage(),
                data
        );
    }

    /**
     * 失败返回结果（自定义消息）
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.FAILED.getCode(),
                message,
                null
        );
    }

    /**
     * 失败返回结果（自定义消息和数据）
     */
    public static <T> Result<T> failed(String message, T data) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.FAILED.getCode(),
                message,
                data
        );
    }

    /**
     * 失败返回结果（默认失败消息）
     */
    public static <T> Result<T> failed() {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.FAILED.getCode(),
                ResultCode.FAILED.getMessage(),
                null
        );
    }

    /**
     * 失败返回结果（处理业务异常）
     */
    public static Result<Object> failed(BusinessException e) {
        return new Result<>(
                e.getHttpStatus().value(),
                e.getCode(),
                e.getUserFriendlyMessage(),
                e.getData()
        );
    }

    // ==================== 特定场景返回方法 ====================

    /**
     * 参数校验失败返回
     */
    public static <T> Result<T> validationFailed(String message) {
        return new Result<>(
                HttpStatus.BAD_REQUEST.value(),
                ResultCode.VALIDATION_FAILED.getCode(),
                message,
                null
        );
    }

    /**
     * 参数校验失败返回（带数据）
     */
    public static <T> Result<T> validationFailed(String message, T data) {
        return new Result<>(
                HttpStatus.BAD_REQUEST.value(),
                ResultCode.VALIDATION_FAILED.getCode(),
                message,
                data
        );
    }

    /**
     * 未授权返回
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(
                HttpStatus.UNAUTHORIZED.value(),
                ResultCode.UNAUTHORIZED.getCode(),
                message,
                null
        );
    }

    /**
     * 权限不足返回
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(
                HttpStatus.FORBIDDEN.value(),
                ResultCode.FORBIDDEN.getCode(),
                message,
                null
        );
    }

    /**
     * 资源不存在返回
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(
                HttpStatus.NOT_FOUND.value(),
                ResultCode.NOT_FOUND.getCode(),
                message,
                null
        );
    }

    /**
     * 用户不存在返回
     */
    public static <T> Result<T> userNotFound(String message) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.USER_NOT_FOUND.getCode(),
                message,
                null
        );
    }

    /**
     * 用户已存在返回
     */
    public static <T> Result<T> userAlreadyExists(String message) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.USER_ALREADY_EXISTS.getCode(),
                message,
                null
        );
    }

    /**
     * 密码错误返回
     */
    public static <T> Result<T> passwordIncorrect(String message) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.PASSWORD_INCORRECT.getCode(),
                message,
                null
        );
    }

    /**
     * 验证码错误返回
     */
    public static <T> Result<T> captchaInvalid(String message) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.CAPTCHA_INVALID.getCode(),
                message,
                null
        );
    }

    /**
     * 验证码过期返回
     */
    public static <T> Result<T> captchaExpired(String message) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.CAPTCHA_EXPIRED.getCode(),
                message,
                null
        );
    }

    /**
     * Token无效返回
     */
    public static <T> Result<T> tokenInvalid(String message) {
        return new Result<>(
                HttpStatus.UNAUTHORIZED.value(),
                ResultCode.TOKEN_INVALID.getCode(),
                message,
                null
        );
    }

    /**
     * Token过期返回
     */
    public static <T> Result<T> tokenExpired(String message) {
        return new Result<>(
                HttpStatus.UNAUTHORIZED.value(),
                ResultCode.TOKEN_EXPIRED.getCode(),
                message,
                null
        );
    }

    /**
     * 业务错误返回
     */
    public static <T> Result<T> businessError(String message) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.BUSINESS_ERROR.getCode(),
                message,
                null
        );
    }

    /**
     * 业务错误返回（带数据）
     */
    public static <T> Result<T> businessError(String message, T data) {
        return new Result<>(
                HttpStatus.OK.value(),
                ResultCode.BUSINESS_ERROR.getCode(),
                message,
                data
        );
    }

    /**
     * 系统繁忙返回
     */
    public static <T> Result<T> systemBusy(String message) {
        return new Result<>(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                ResultCode.SYSTEM_BUSY.getCode(),
                message,
                null
        );
    }

    /**
     * 频率限制返回
     */
    public static <T> Result<T> rateLimitExceeded(String message) {
        return new Result<>(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                ResultCode.RATE_LIMIT_EXCEEDED.getCode(),
                message,
                null
        );
    }

    // ==================== 自定义返回方法 ====================

    /**
     * 自定义返回结果（完整参数）
     */
    public static <T> Result<T> custom(int httpStatus, int code, String message, T data) {
        return new Result<>(httpStatus, code, message, data);
    }

    /**
     * 自定义返回结果（HTTP状态码+错误码）
     */
    public static <T> Result<T> custom(HttpStatus httpStatus, ResultCode resultCode) {
        return new Result<>(
                httpStatus.value(),
                resultCode.getCode(),
                resultCode.getMessage(),
                null
        );
    }

    /**
     * 自定义返回结果（HTTP状态码+错误码+数据）
     */
    public static <T> Result<T> custom(HttpStatus httpStatus, ResultCode resultCode, T data) {
        return new Result<>(
                httpStatus.value(),
                resultCode.getCode(),
                resultCode.getMessage(),
                data
        );
    }

    /**
     * 自定义返回结果（错误码+自定义消息）
     */
    public static <T> Result<T> custom(ResultCode resultCode, String message) {
        return new Result<>(
                HttpStatus.OK.value(),
                resultCode.getCode(),
                message,
                null
        );
    }

    /**
     * 自定义返回结果（错误码+自定义消息+数据）
     */
    public static <T> Result<T> custom(ResultCode resultCode, String message, T data) {
        return new Result<>(
                HttpStatus.OK.value(),
                resultCode.getCode(),
                message,
                data
        );
    }

    // ==================== of 静态工厂方法 ====================

    /**
     * 创建成功结果（数据）
     */
    public static <T> Result<T> of(T data) {
        return success(data);
    }

    /**
     * 创建成功结果（数据 + 消息）
     */
    public static <T> Result<T> of(T data, String message) {
        return success(data, message);
    }

    /**
     * 创建失败结果（错误码）
     */
    public static <T> Result<T> of(ResultCode resultCode) {
        return failed(resultCode);
    }

    /**
     * 创建失败结果（错误码 + 数据）
     */
    public static <T> Result<T> of(ResultCode resultCode, T data) {
        return failed(resultCode, data);
    }

    /**
     * 创建失败结果（错误码 + 消息）
     */
    public static <T> Result<T> of(ResultCode resultCode, String message) {
        return custom(resultCode, message);
    }

    /**
     * 创建失败结果（错误码 + 消息 + 数据）
     */
    public static <T> Result<T> of(ResultCode resultCode, String message, T data) {
        return custom(resultCode, message, data);
    }

    /**
     * 创建失败结果（消息）
     */
    public static <T> Result<T> of(String message) {
        return failed(message);
    }

    /**
     * 创建失败结果（消息 + 数据）
     */
    public static <T> Result<T> of(String message, T data) {
        return failed(message, data);
    }

    /**
     * 创建结果（HTTP状态码 + 错误码）
     */
    public static <T> Result<T> of(HttpStatus httpStatus, ResultCode resultCode) {
        return failed(httpStatus, resultCode);
    }

    /**
     * 创建结果（HTTP状态码 + 错误码 + 数据）
     */
    public static <T> Result<T> of(HttpStatus httpStatus, ResultCode resultCode, T data) {
        return new Result<>(
                httpStatus.value(),
                resultCode.getCode(),
                resultCode.getMessage(),
                data
        );
    }

    /**
     * 创建结果（HTTP状态码 + 错误码 + 消息）
     */
    public static <T> Result<T> of(HttpStatus httpStatus, ResultCode resultCode, String message) {
        return new Result<>(
                httpStatus.value(),
                resultCode.getCode(),
                message,
                null
        );
    }

    /**
     * 创建结果（HTTP状态码 + 错误码 + 消息 + 数据）
     */
    public static <T> Result<T> of(HttpStatus httpStatus, ResultCode resultCode, String message, T data) {
        return new Result<>(
                httpStatus.value(),
                resultCode.getCode(),
                message,
                data
        );
    }

    /**
     * 创建结果（HTTP状态码 + 错误码 + 消息 + 数据）- 完整参数
     */
    public static <T> Result<T> of(int httpStatus, int code, String message, T data) {
        return custom(httpStatus, code, message, data);
    }

    /**
     * 创建结果（处理业务异常）
     */
    public static Result<Object> of(BusinessException exception) {
        return failed(exception);
    }
}
