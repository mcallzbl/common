package com.mcallzbl.common.exception;

import com.mcallzbl.common.BusinessException;
import com.mcallzbl.common.Result;
import com.mcallzbl.common.annotation.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理应用中的异常，返回统一的响应格式
 */
@RestControllerAdvice
@ResponseWrapper
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result<Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("Business Exception - User Message: {} - Request URI: {}",
                e.getUserFriendlyMessage(), request.getRequestURI(), e);
        return Result.failed(e);
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> handleException(Exception e, HttpServletRequest request) {
        log.error("System Exception - Message: {} - Request URI: {}",
                e.getMessage(), request.getRequestURI(), e);
        return Result.failed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result<Object> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.error("Illegal Argument Exception - Message: {} - Request URI: {}",
                e.getMessage(), request.getRequestURI(), e);
        return Result.validationFailed(e.getMessage());
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public Result<Object> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("Null Pointer Exception - Request URI: {}", request.getRequestURI(), e);
        return Result.failed(HttpStatus.INTERNAL_SERVER_ERROR, "系统内部错误");
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result<Object> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("Runtime Exception - Message: {} - Request URI: {}",
                e.getMessage(), request.getRequestURI(), e);
        return Result.failed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result<Object> handleHttpRequestMethodNotSupportedException(
            org.springframework.web.HttpRequestMethodNotSupportedException e,
            HttpServletRequest request) {
        log.error("Method Not Supported - Request URI: {} - Method: {}",
                request.getRequestURI(), request.getMethod(), e);
        return Result.failed(HttpStatus.METHOD_NOT_ALLOWED, "请求方法不支持");
    }

    /**
     * 处理媒体类型不支持异常
     */
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public Result<Object> handleHttpMediaTypeNotSupportedException(
            org.springframework.web.HttpMediaTypeNotSupportedException e,
            HttpServletRequest request) {
        log.error("Media Type Not Supported - Request URI: {}", request.getRequestURI(), e);
        return Result.failed(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "不支持的媒体类型");
    }

    /**
     * 处理请求体缺失异常
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseBody
    public Result<Object> handleHttpMessageNotReadableException(
            org.springframework.http.converter.HttpMessageNotReadableException e,
            HttpServletRequest request) {
        log.error("Message Not Readable - Request URI: {}", request.getRequestURI(), e);
        return Result.validationFailed("请求体格式错误或缺失");
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    @ResponseBody
    public Result<Object> handleMissingServletRequestParameterException(
            org.springframework.web.bind.MissingServletRequestParameterException e,
            HttpServletRequest request) {
        log.error("Missing Parameter - Request URI: {} - Parameter: {}",
                request.getRequestURI(), e.getParameterName(), e);
        return Result.validationFailed(String.format("缺少必需的请求参数: %s", e.getParameterName()));
    }

    /**
     * 处理方法参数无效异常（用于参数校验）
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<Object> handleMethodArgumentNotValidException(
            org.springframework.web.bind.MethodArgumentNotValidException e,
            HttpServletRequest request) {
        log.error("Method Argument Not Valid - Request URI: {}", request.getRequestURI(), e);

        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("参数校验失败");

        return Result.validationFailed(errorMessage);
    }

    /**
     * 处理请求参数绑定异常
     */
    @ExceptionHandler(org.springframework.validation.BindException.class)
    @ResponseBody
    public Result<Object> handleBindException(
            org.springframework.validation.BindException e,
            HttpServletRequest request) {
        log.error("Bind Exception - Request URI: {}", request.getRequestURI(), e);

        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("参数绑定失败");

        return Result.validationFailed(errorMessage);
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException(
            org.springframework.web.servlet.resource.NoResourceFoundException e,
            HttpServletRequest request) {
        log.error("Resource Not Found - Request URI: {} - Resource Path: {}",
                request.getRequestURI(), e.getResourcePath(), e);

        // 返回404页面
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(org.springframework.http.MediaType.TEXT_HTML)
                .body("<script>window.location.href='/error/404.html'</script>");
    }
}