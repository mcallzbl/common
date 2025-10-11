package com.mcallzbl.common.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcallzbl.common.Result;
import com.mcallzbl.common.annotation.NoResponseWrapper;
import com.mcallzbl.common.annotation.ResponseWrapper;
import com.mcallzbl.common.dto.CommonResultTransferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应包装器
 * 自动包装Controller返回值为统一格式
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("正在处理方法返回值...");

        // 检查是否标记了NoResponseWrapper注解
        if (returnType.getDeclaringClass().isAnnotationPresent(NoResponseWrapper.class) ||
                returnType.hasMethodAnnotation(NoResponseWrapper.class)) {
            log.debug("发现@NoResponseWrapper注解，跳过响应包装");
            return false;
        }

        // 检查返回类型是否为ResponseEntity
        if (ResponseEntity.class.isAssignableFrom(returnType.getParameterType())) {
            log.debug("返回类型为ResponseEntity，跳过响应包装");
            return false;
        }

        // 检查是否有ResponseWrapper注解
        boolean hasClassAnnotation = returnType.getDeclaringClass().isAnnotationPresent(ResponseWrapper.class);
        boolean hasMethodAnnotation = returnType.hasMethodAnnotation(ResponseWrapper.class);

        if (hasClassAnnotation || hasMethodAnnotation) {
            log.debug("找到@ResponseWrapper注解，将对响应进行包装: {}.{}",
                    returnType.getDeclaringClass().getSimpleName(),
                    returnType.getMethod() != null ? returnType.getMethod().getName() : "unknown");
            return true;
        }

        log.debug("未找到@ResponseWrapper注解，跳过响应包装");
        return false;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response
    ) {
        // 双重检查是否应该包装响应
        if (!shouldWrapResponse(returnType)) {
            log.debug("双重检查失败，跳过响应包装: {}.{}",
                    returnType.getDeclaringClass().getSimpleName(),
                    returnType.getMethod() != null ? returnType.getMethod().getName() : "unknown");
            return body;
        }

        try {
            // 如果已经是Result类型，直接转换为传输DTO
            if (body instanceof Result<?> result) {
                // 设置HTTP状态码
                HttpStatus httpStatus = HttpStatus.valueOf(result.getHttpStatus());
                response.setStatusCode(httpStatus);

                log.debug("包装Result对象: code={}, message={}, data={}",
                        result.getCode(), result.getMessage(), result.getData());

                return CommonResultTransferDTO.from(result);
            }

            // 处理String返回类型的特殊情况
            if (body instanceof String) {
                Result<String> result = Result.success((String) body);
                CommonResultTransferDTO<String> transferDTO = CommonResultTransferDTO.from(result);

                try {
                    String jsonString = objectMapper.writeValueAsString(transferDTO);
                    log.debug("包装String响应: {}", jsonString);
                    return jsonString;
                } catch (JsonProcessingException e) {
                    log.error("序列化响应失败，返回原始内容: {}", e.getMessage());
                    return body;
                }
            }

            // 包装其他类型的返回值
            Result<Object> result = Result.success(body);
            CommonResultTransferDTO<Object> transferDTO = CommonResultTransferDTO.from(result);

            log.debug("包装对象响应: code={}, message={}, data={}",
                    transferDTO.getCode(), transferDTO.getMessage(), transferDTO.getData());

            return transferDTO;

        } catch (Exception e) {
            log.error("响应包装过程中发生异常，返回原始内容: {}", e.getMessage(), e);
            return body;
        }
    }

    /**
     * 检查是否应该包装响应（用于双重验证）
     */
    private boolean shouldWrapResponse(MethodParameter returnType) {
        // 检查NoResponseWrapper注解
        if (returnType.getDeclaringClass().isAnnotationPresent(NoResponseWrapper.class) ||
                returnType.hasMethodAnnotation(NoResponseWrapper.class)) {
            return false;
        }

        // 检查ResponseEntity类型
        if (ResponseEntity.class.isAssignableFrom(returnType.getParameterType())) {
            return false;
        }

        // 检查ResponseWrapper注解
        boolean hasClassAnnotation = returnType.getDeclaringClass().isAnnotationPresent(ResponseWrapper.class);
        boolean hasMethodAnnotation = returnType.hasMethodAnnotation(ResponseWrapper.class);

        return hasClassAnnotation || hasMethodAnnotation;
    }
}