package com.mcallzbl.user.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcallzbl.common.ResultCode;
import com.mcallzbl.common.dto.CommonResultTransferVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义访问拒绝处理器
 * 处理权限不足请求的响应
 *
 * @author mcallzbl
 * @since 2025-11-17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.warn("权限不足访问被拒绝: {} - {}", request.getRequestURI(), accessDeniedException.getMessage());

        // 设置响应头
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 构建统一响应格式
        CommonResultTransferVO<Void> result = CommonResultTransferVO.failed(
                ResultCode.FORBIDDEN.getCode(),
                "权限不足，无法访问该资源"
        );

        // 写入响应
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}