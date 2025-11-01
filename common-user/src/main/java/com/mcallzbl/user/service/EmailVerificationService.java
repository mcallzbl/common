package com.mcallzbl.user.service;

import com.mcallzbl.user.pojo.request.VerificationEmailRequest;
import com.mcallzbl.user.pojo.response.VerificationEmailResponse;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
public interface EmailVerificationService {
    /**
     * 发送邮件验证码
     *
     * @param request 邮件验证请求
     * @return 邮件验证响应
     */
    VerificationEmailResponse sendVerificationCode(VerificationEmailRequest request);

    /**
     * 验证邮件验证码
     *
     * @param email   邮箱
     * @param code    验证码
     * @param purpose 验证目的
     * @return 是否验证成功
     */
    boolean verifyCode(String email, String code, VerificationEmailRequest.Purpose purpose);
}