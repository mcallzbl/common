package com.mcallzbl.user.service;

import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.pojo.request.LoginRequest;
import com.mcallzbl.user.pojo.response.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/25
 */
public interface AuthService {
    User login(LoginRequest loginRequest);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新Token
     * @param request      HTTP请求对象（用于获取设备信息等）
     * @param response     HTTP响应对象（用于更新Cookie）
     * @return 新的访问令牌和刷新令牌
     */
    RefreshTokenResponse refreshToken(String refreshToken, HttpServletRequest request, HttpServletResponse response);

    /**
     * 用户登出
     *
     * @param accessToken  访问令牌
     * @param refreshToken 刷新令牌
     * @param response     HTTP响应对象（用于清除Cookie）
     * @return 登出结果VO
     */

    String logout(String accessToken, String refreshToken, HttpServletResponse response);
}