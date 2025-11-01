package com.mcallzbl.user.service.impl;

import com.mcallzbl.common.BusinessException;
import com.mcallzbl.common.ResultCode;
import com.mcallzbl.user.mapper.UserMapper;
import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.pojo.request.LoginRequest;
import com.mcallzbl.user.pojo.request.VerificationEmailRequest;
import com.mcallzbl.user.pojo.response.LoginResponse;
import com.mcallzbl.user.pojo.response.RefreshTokenResponse;
import com.mcallzbl.user.service.AuthService;
import com.mcallzbl.user.service.EmailVerificationService;
import com.mcallzbl.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final EmailVerificationService emailVerificationService;
    private final UserMapper userMapper;
    private final UserService userService;

    @Override
    public User login(LoginRequest loginRequest) {
        User user;
        // 根据密码或验证码是否存在来判断登录方式
        if (StringUtils.hasText(loginRequest.getVerificationCode())) {
            user = handleEmailCodeLogin(loginRequest);
        } else {
            throw new BusinessException(ResultCode.VALIDATION_FAILED, "验证码必须提供");
        }
        return user;
    }

    private User handleEmailCodeLogin(LoginRequest loginDTO) {
        boolean isCodeValid = emailVerificationService.verifyCode(
                loginDTO.getEmail(),
                loginDTO.getVerificationCode(),
                VerificationEmailRequest.Purpose.LOGIN
        );

        if (!isCodeValid) {
            throw new BusinessException(ResultCode.EMAIL_VERIFICATION_CODE_ERROR, "邮箱验证码错误或已过期");
        }

        User user = userMapper.selectByEmail(loginDTO.getEmail());

        if (user == null) {
            log.info("用户不存在，将通过邮箱验证码自动注册：email={}", loginDTO.getEmail());

            user = userService.createUserByEmail(loginDTO.getEmail());
            if (user == null) {
                throw new BusinessException(ResultCode.FAILED, "用户自动注册失败，请稍后再试");
            }
            log.info("用户自动注册成功：userId={}, email={}", user.getId(), user.getEmail());
        }
        if (!user.isActive()) {
            throw new BusinessException("用户已被禁用");
        }
        return user;
    }


    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新Token
     * @param request      HTTP请求对象（用于获取设备信息等）
     * @param response     HTTP响应对象（用于更新Cookie）
     * @return 新的访问令牌和刷新令牌
     */
    @Override
    public RefreshTokenResponse refreshToken(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    /**
     * 用户登出
     *
     * @param accessToken  访问令牌
     * @param refreshToken 刷新令牌
     * @param response     HTTP响应对象（用于清除Cookie）
     * @return 登出结果VO
     */
    @Override
    public String logout(String accessToken, String refreshToken, HttpServletResponse response) {
        return "";
    }
}