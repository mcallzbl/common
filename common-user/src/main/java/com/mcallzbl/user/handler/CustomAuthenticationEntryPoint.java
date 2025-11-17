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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义认证入口点
 * 处理未认证请求的响应
 *
 * @author mcallzbl
 * @since 2025-11-17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.warn("未认证访问被拒绝: {} - {}", request.getRequestURI(), authException.getMessage());

        // 设置响应头
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 构建统一响应格式
        CommonResultTransferVO<Void> result = CommonResultTransferVO.failed(
                ResultCode.UNAUTHORIZED.getCode(),
                "访问被拒绝，请先登录"
        );

        // 写入响应
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}