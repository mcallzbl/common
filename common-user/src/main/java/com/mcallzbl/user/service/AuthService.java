package com.mcallzbl.user.service;

import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.pojo.request.EmailLoginRequest;
import com.mcallzbl.user.pojo.request.UsernameLoginRequest;
import com.mcallzbl.user.pojo.request.UsernameRegistrationRequest;

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
     * 用户名注册
     *
     * @param usernameRegistrationRequest 用户名注册请求
     * @return 注册成功的用户信息
     */
    User registerByUsername(UsernameRegistrationRequest usernameRegistrationRequest);

}