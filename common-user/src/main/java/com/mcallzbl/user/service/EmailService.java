package com.mcallzbl.user.service;

import com.mcallzbl.user.pojo.request.VerificationEmailRequest;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
public interface EmailService {
    /**
     * 发送验证码邮件
     *
     * @param to      收件人邮箱
     * @param code    验证码
     * @param purpose 验证目的
     */
    void sendVerificationCode(String to, String code, VerificationEmailRequest.Purpose purpose);
}