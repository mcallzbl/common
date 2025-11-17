package com.mcallzbl.user.service;

import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.pojo.request.EmailLoginRequest;
import com.mcallzbl.user.pojo.request.UsernameLoginRequest;
import com.mcallzbl.user.pojo.response.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 认证服务接口
 * 支持邮箱登录和用户名登录
 *
 * @author mcallzbl
 * @version 2.0
 * @since 2025/10/25
 */
public interface AuthService {

    /**
     * 邮箱登录
     *
     * @param emailLoginRequest 邮箱登录请求
     * @return 用户信息
     */
    User loginByEmail(EmailLoginRequest emailLoginRequest);

    /**
     * 用户名登录
     *
     * @param usernameLoginRequest 用户名登录请求
     * @return 用户信息
     */
    User loginByUsername(UsernameLoginRequest usernameLoginRequest);

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