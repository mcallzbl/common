package com.mcallzbl.common.exception;

import com.mcallzbl.common.ResultCode;
import com.mcallzbl.common.util.CommonI18nUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化业务异常处理器
 * 提供支持多语言的业务异常创建方法
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class I18nBusinessException {

    private final CommonI18nUtils commonI18NUtils;

    /**
     * 创建国际化业务异常
     *
     * @param resultCode 错误码枚举
     * @return 业务异常
     */
    public com.mcallzbl.common.BusinessException create(ResultCode resultCode) {
        String localizedMessage = commonI18NUtils.getMessage(resultCode.getMessageKey());
        return new com.mcallzbl.common.BusinessException(HttpStatus.OK, resultCode.getCode(), localizedMessage);
    }

    /**
     * 创建国际化业务异常（带参数）
     *
     * @param resultCode 错误码枚举
     * @param args       消息参数
     * @return 业务异常
     */
    public com.mcallzbl.common.BusinessException create(ResultCode resultCode, Object... args) {
        String localizedMessage = commonI18NUtils.getMessage(resultCode.getMessageKey(), args);
        return new com.mcallzbl.common.BusinessException(HttpStatus.OK, resultCode.getCode(), localizedMessage);
    }

    /**
     * 创建国际化业务异常（指定语言）
     *
     * @param resultCode 错误码枚举
     * @param language   语言代码
     * @return 业务异常
     */
    public com.mcallzbl.common.BusinessException create(ResultCode resultCode, String language) {
        String localizedMessage = commonI18NUtils.getMessage(resultCode.getMessageKey(), language);
        return new com.mcallzbl.common.BusinessException(HttpStatus.OK, resultCode.getCode(), localizedMessage);
    }

    /**
     * 创建国际化业务异常（指定语言，带参数）
     *
     * @param resultCode 错误码枚举
     * @param language   语言代码
     * @param args       消息参数
     * @return 业务异常
     */
    public com.mcallzbl.common.BusinessException create(ResultCode resultCode, String language, Object... args) {
        String localizedMessage = commonI18NUtils.getMessage(resultCode.getMessageKey(), language, args);
        return new com.mcallzbl.common.BusinessException(HttpStatus.OK, resultCode.getCode(), localizedMessage);
    }

    /**
     * 创建国际化业务异常（指定Locale）
     *
     * @param resultCode 错误码枚举
     * @param locale     Locale对象
     * @return 业务异常
     */
    public com.mcallzbl.common.BusinessException create(ResultCode resultCode, Locale locale) {
        String localizedMessage = commonI18NUtils.getMessage(resultCode.getMessageKey(), locale);
        return new com.mcallzbl.common.BusinessException(HttpStatus.OK, resultCode.getCode(), localizedMessage);
    }

    /**
     * 创建国际化业务异常（指定Locale，带参数）
     *
     * @param resultCode 错误码枚举
     * @param locale     Locale对象
     * @param args       消息参数
     * @return 业务异常
     */
    public com.mcallzbl.common.BusinessException create(ResultCode resultCode, Locale locale, Object... args) {
        String localizedMessage = commonI18NUtils.getMessage(resultCode.getMessageKey(), args, locale);
        return new com.mcallzbl.common.BusinessException(HttpStatus.OK, resultCode.getCode(), localizedMessage);
    }

    /**
     * 创建国际化业务异常（HTTP状态码 + 错误码）
     *
     * @param httpStatus HTTP状态码
     * @param resultCode 错误码枚举
     * @return 业务异常
     */
    public com.mcallzbl.common.BusinessException create(HttpStatus httpStatus, ResultCode resultCode) {
        String localizedMessage = commonI18NUtils.getMessage(resultCode.getMessageKey());
        return new com.mcallzbl.common.BusinessException(httpStatus, resultCode.getCode(), localizedMessage);
    }

    /**
     * 创建国际化业务异常（HTTP状态码 + 错误码 + 数据）
     *
     * @param httpStatus HTTP状态码
     * @param resultCode 错误码枚举
     * @param data       附加数据
     * @return 业务异常
     */
    public com.mcallzbl.common.BusinessException create(HttpStatus httpStatus, ResultCode resultCode, Object data) {
        String localizedMessage = commonI18NUtils.getMessage(resultCode.getMessageKey());
        return new com.mcallzbl.common.BusinessException(httpStatus, resultCode.getCode(), localizedMessage, data);
    }

    // ==================== 便捷方法 ====================

    /**
     * 验证码错误异常
     */
    public com.mcallzbl.common.BusinessException verificationCodeRequired() {
        return create(ResultCode.VALIDATION_FAILED, "verification.code.required");
    }

    /**
     * 邮箱验证码错误异常
     */
    public com.mcallzbl.common.BusinessException emailVerificationCodeError() {
        return create(ResultCode.EMAIL_VERIFICATION_CODE_ERROR);
    }

    /**
     * 用户不存在异常
     */
    public com.mcallzbl.common.BusinessException userNotFound() {
        return create(ResultCode.USER_NOT_FOUND);
    }

    /**
     * 用户已存在异常
     */
    public com.mcallzbl.common.BusinessException userAlreadyExists() {
        return create(ResultCode.USER_ALREADY_EXISTS);
    }

    /**
     * 密码错误异常
     */
    public com.mcallzbl.common.BusinessException passwordIncorrect() {
        return create(ResultCode.PASSWORD_INCORRECT);
    }

    /**
     * 系统繁忙异常
     */
    public com.mcallzbl.common.BusinessException systemBusy() {
        return create(HttpStatus.SERVICE_UNAVAILABLE, ResultCode.SYSTEM_BUSY);
    }

    /**
     * 频率限制异常
     */
    public com.mcallzbl.common.BusinessException rateLimitExceeded() {
        return create(HttpStatus.TOO_MANY_REQUESTS, ResultCode.RATE_LIMIT_EXCEEDED);
    }
}