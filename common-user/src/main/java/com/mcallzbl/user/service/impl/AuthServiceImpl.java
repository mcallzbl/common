package com.mcallzbl.user.service.impl;

import com.mcallzbl.common.BusinessException;
import com.mcallzbl.common.ResultCode;
import com.mcallzbl.user.mapper.UserMapper;
import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.pojo.request.LoginRequest;
import com.mcallzbl.user.pojo.request.VerificationEmailRequest;
import com.mcallzbl.user.pojo.response.RefreshTokenResponse;
import com.mcallzbl.user.service.AuthService;
import com.mcallzbl.user.service.EmailVerificationService;
import com.mcallzbl.user.service.UserService;
import com.mcallzbl.user.context.IpContext;
import com.mcallzbl.user.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(LoginRequest loginRequest) {
        // 获取当前请求的IP地址（从拦截器设置的ThreadLocal中获取）
        String clientIp = IpContext.getIpOrDefault("127.0.0.1");
        log.info("用户登录尝试 - 邮箱: {}, IP地址: {}", loginRequest.getEmail(), clientIp);

        User user;
        // 根据密码或验证码是否存在来判断登录方式
        if (StringUtils.hasText(loginRequest.getVerificationCode())) {
            user = handleEmailCodeLogin(loginRequest);
        } else if(StringUtils.hasText(loginRequest.getPassword())) {
            user = handlePasswordLogin(loginRequest);
        }else {
            throw new BusinessException(ResultCode.VALIDATION_FAILED, "非法的登录请求");
        }

        // 更新用户登录信息
        user.updateLoginInfo(clientIp);

        // 保存到数据库
        int updateResult = userMapper.updateById(user);
        if (updateResult > 0) {
            log.info("用户登录成功 - userId: {}, 邮箱: {}, 登录IP: {}, 登录次数: {}",
                    user.getId(), user.getEmail(), clientIp, user.getLoginCount());
        } else {
            log.warn("更新用户登录信息失败 - userId: {}", user.getId());
        }

        return user;
    }

    private User handlePasswordLogin(LoginRequest loginRequest) {
        User user = userMapper.selectByEmail(loginRequest.getEmail());
        if (user == null || !StringUtils.hasText(user.getPasswordHash())
                || !passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw BusinessException.of("邮箱或密码不正确");
        }
        return user;
    }

    private User handleEmailCodeLogin(LoginRequest loginDTO) {
        boolean isCodeValid = emailVerificationService.verifyCode(
                loginDTO.getEmail(),
                loginDTO.getVerificationCode(),
                VerificationEmailRequest.Purpose.LOGIN
        );
// TODO 屏蔽邮件验证码验证
//        if (!isCodeValid) {
//            throw new BusinessException(ResultCode.EMAIL_VERIFICATION_CODE_ERROR, "邮箱验证码错误或已过期");
//        }

        User user = userMapper.selectByEmail(loginDTO.getEmail());

        if (user == null) {
            log.info("用户不存在，将通过邮箱验证码自动注册：email={}", loginDTO.getEmail());

            user = userService.createUserByEmail(loginDTO.getEmail());
            if (user == null) {
                throw new BusinessException(ResultCode.FAILED, "用户自动注册失败，请稍后再试");
            }
            log.info("用户自动注册成功：userId={}, email={}", user.getId(), user.getEmail());
        }
        if (user.isInActive()) {
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