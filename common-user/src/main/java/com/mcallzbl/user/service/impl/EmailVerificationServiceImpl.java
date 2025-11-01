package com.mcallzbl.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.mcallzbl.common.BusinessException;
import com.mcallzbl.user.pojo.request.VerificationEmailRequest;
import com.mcallzbl.user.pojo.response.VerificationEmailResponse;
import com.mcallzbl.user.service.EmailService;
import com.mcallzbl.user.service.EmailVerificationService;
import com.mcallzbl.user.util.EmailI18nUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.concurrent.TimeUnit;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private static final String VERIFICATION_CODE_PREFIX = "email_verification:";
    private static final String SEND_LIMIT_PREFIX = "email_send_limit:";
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRE_MINUTES = 5; // 验证码5分钟过期
    private static final int SEND_LIMIT_SECONDS = 60; // 发送限制60秒
    private final EmailService emailService;
    private final StringRedisTemplate redisTemplate;
    private final EmailI18nUtils emailI18nUtils;

    @Override
    public VerificationEmailResponse sendVerificationCode(VerificationEmailRequest dto) {
        String email = dto.getEmail();
        VerificationEmailRequest.Purpose purpose = dto.getPurpose();

        // 检查发送频率限制
        checkSendLimit(email, purpose.getValue());

        // 生成6位数字验证码
        String code = RandomUtil.randomNumbers(CODE_LENGTH);

        try {
            // 发送邮件
            emailService.sendVerificationCode(email, code, purpose);

            // 存储验证码到Redis
            String key = buildVerificationKey(email, purpose.getValue());
            redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

            // 设置发送频率限制
            setSendLimit(email, purpose.getValue());

            log.info("邮件验证码发送成功：邮箱={}, 目的={}", email, purpose.getValue());

            return VerificationEmailResponse.builder()
                    .email(email)
                    .expireTime(System.currentTimeMillis() + CODE_EXPIRE_MINUTES * 60 * 1000L)
                    .build();

        } catch (Exception e) {
            log.error("邮件验证码发送失败：邮箱={}, 目的={}", email, purpose, e);
            throw new BusinessException(emailI18nUtils.getMessage("email.verification.send.failed"));
        }
    }

    @Override
    public boolean verifyCode(String email, String code, VerificationEmailRequest.Purpose purpose) {
        String key = buildVerificationKey(email, purpose.getValue());
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode == null) {
            log.warn("验证码已过期或不存在：邮箱={}, 目的={}", email, purpose.getValue());
            return false;
        }

        boolean isValid = storedCode.equals(code);
        if (isValid) {
            // 验证成功后删除验证码
            redisTemplate.delete(key);
            log.info("邮件验证码验证成功：邮箱={}, 目的={}", email, purpose.getValue());
        } else {
            log.warn("邮件验证码验证失败：邮箱={}, 目的={}, 提供的验证码={}", email, purpose.getValue(), code);
        }

        return isValid;
    }

    private void checkSendLimit(String email, String purpose) {
        String limitKey = buildSendLimitKey(email, purpose);
        String lastSendTime = redisTemplate.opsForValue().get(limitKey);

        if (lastSendTime != null) {
            throw new BusinessException(emailI18nUtils.getMessage("email.send.limit.exceeded"));
        }
    }

    private void setSendLimit(String email, String purpose) {
        String limitKey = buildSendLimitKey(email, purpose);
        redisTemplate.opsForValue().set(limitKey,
                String.valueOf(System.currentTimeMillis()),
                SEND_LIMIT_SECONDS,
                TimeUnit.SECONDS);
    }

    private String buildVerificationKey(String email, String purpose) {
        return VERIFICATION_CODE_PREFIX + email + ":" + purpose;
    }

    private String buildSendLimitKey(String email, String purpose) {
        return SEND_LIMIT_PREFIX + email + ":" + purpose;
    }
}