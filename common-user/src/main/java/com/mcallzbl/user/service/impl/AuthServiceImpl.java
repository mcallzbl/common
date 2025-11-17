package com.mcallzbl.user.service.impl;

import com.mcallzbl.common.BusinessException;
import com.mcallzbl.common.ResultCode;
import com.mcallzbl.user.context.IpContext;
import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.pojo.request.EmailLoginRequest;
import com.mcallzbl.user.pojo.request.UsernameLoginRequest;
import com.mcallzbl.user.pojo.request.VerificationEmailRequest;
import com.mcallzbl.user.service.AuthService;
import com.mcallzbl.user.service.EmailVerificationService;
import com.mcallzbl.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User loginByEmail(EmailLoginRequest emailLoginRequest) {
        String clientIp = IpContext.getIpOrDefault("127.0.0.1");
        log.info("用户登录尝试 - 邮箱: {}, IP地址: {}", emailLoginRequest.getEmail(), clientIp);

        //TODO 登录频率限制
        User user;
        if (StringUtils.hasText(emailLoginRequest.getVerificationCode())) {
            user = handleEmailCodeLogin(emailLoginRequest);
        } else if (StringUtils.hasText(emailLoginRequest.getPassword())) {
            user = handleEmailPasswordLogin(emailLoginRequest);
        } else {
            throw new BusinessException(ResultCode.VALIDATION_FAILED, "非法的登录请求");
        }

        // 更新用户登录信息并保存
        return updateUserLoginInfo(user, clientIp, "邮箱", user.getEmail());
    }

    /**
     * 用户名登录
     *
     * @param usernameLoginRequest 用户名登录请求
     * @return 用户信息
     */
    @Override
    @Transactional
    public User loginByUsername(UsernameLoginRequest usernameLoginRequest) {
        String clientIp = IpContext.getIpOrDefault("127.0.0.1");
        log.info("用户登录尝试 - 用户名: {}, IP地址: {}", usernameLoginRequest.getUsername(), clientIp);

        // TODO 登录频率限制

        // 使用验证方法获取用户（确保用户存在且可用）
        User user = userService.getUserByUsername(usernameLoginRequest.getUsername());

        // 验证密码
        validateUserPassword(user, usernameLoginRequest.getPassword());

        // 更新用户登录信息并保存
        return updateUserLoginInfo(user, clientIp, "用户名", user.getUsername());
    }

    // ==================== 私有方法 ====================

    /**
     * 更新用户登录信息并保存到数据库
     *
     * @param user       用户对象
     * @param clientIp   客户端IP
     * @param loginType  登录类型（用于日志）
     * @param loginValue 登录值（用于日志）
     * @return 更新后的用户信息
     */
    private User updateUserLoginInfo(User user, String clientIp, String loginType, String loginValue) {
        // 更新用户登录信息
        user.updateLoginInfo(clientIp);

        // 保存到数据库
        boolean updateSuccess = userService.updateUser(user);
        if (updateSuccess) {
            log.info("用户登录成功 - userId: {}, {}: {}, 登录IP: {}, 登录次数: {}",
                    user.getId(), loginType, loginValue, clientIp, user.getLoginCount());
        } else {
            log.warn("更新用户登录信息失败 - userId: {}", user.getId());
            throw BusinessException.of("登录失败");
        }

        return user;
    }

    /**
     * 邮箱密码登录处理
     *
     * @param emailLoginRequest 邮箱登录请求
     * @return 用户信息
     */
    private User handleEmailPasswordLogin(EmailLoginRequest emailLoginRequest) {
        // 使用直接查询方法，因为需要手动处理用户不存在的情况
        User user = userService.findUserByEmail(emailLoginRequest.getEmail());
        if (user == null) {
            throw BusinessException.of("邮箱或密码不正确");
        }

        // 验证密码（包含用户状态验证）
        validateUserPassword(user, emailLoginRequest.getPassword());
        return user;
    }

    /**
     * 通用密码验证方法
     * 验证密码正确性和用户状态
     *
     * @param user     用户对象
     * @param password 待验证的密码
     */
    private void validateUserPassword(User user, String password) {
        if (user == null || !StringUtils.hasText(user.getPasswordHash())) {
            throw BusinessException.of("用户不存在或密码未设置");
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw BusinessException.of("密码不正确");
        }

        // 用户状态验证已经在UserService.getUserXX方法中处理了
        // 这里不需要重复验证，除非有特殊需求
    }

    private User handleEmailCodeLogin(EmailLoginRequest loginDTO) {
        boolean isCodeValid = emailVerificationService.verifyCode(
                loginDTO.getEmail(),
                loginDTO.getVerificationCode(),
                VerificationEmailRequest.Purpose.LOGIN
        );
        if (!isCodeValid) {
            throw new BusinessException(ResultCode.EMAIL_VERIFICATION_CODE_ERROR, "邮箱验证码错误或已过期");
        }

        User user = userService.getUserByEmail(loginDTO.getEmail());

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


}