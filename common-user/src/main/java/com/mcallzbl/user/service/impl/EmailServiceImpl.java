package com.mcallzbl.user.service.impl;

import com.mcallzbl.common.BusinessException;
import com.mcallzbl.user.pojo.request.VerificationEmailRequest;
import com.mcallzbl.user.service.EmailService;
import com.mcallzbl.user.util.EmailI18nUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine emailTemplateEngine;
    private final EmailI18nUtils emailI18nUtils;

    @Value("${spring.mail.username:noreply@userservice.com}")
    private String fromEmail;

    @Async
    @Override
    public void sendVerificationCode(String to, String code, VerificationEmailRequest.Purpose purpose) {
        log.debug("[com.mcallzbl.user.service.impl.EmailServiceImpl.sendVerificationCode()]" +
                " params: to={}, code={}, purpose={}", to, code, purpose);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);

            String subject = getSubjectByPurpose(purpose);
            String content = buildEmailContent(purpose, code);

            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(mimeMessage);
            log.info("邮件验证码发送成功，收件人：{}，目的：{}", to, purpose);
        } catch (MessagingException e) {
            log.error("邮件发送失败，收件人：{}，目的：{}", to, purpose, e);
            throw new BusinessException(emailI18nUtils.getMessage("email.send.failed"));
        }
    }

    private String getSubjectByPurpose(VerificationEmailRequest.Purpose purpose) {
        return switch (purpose) {
//            case REGISTER -> emailI18nUtils.getMessage("email.subject.register");
            case RESET_PASSWORD -> emailI18nUtils.getMessage("email.subject.reset.password");
            case CHANGE_EMAIL -> emailI18nUtils.getMessage("email.subject.change.email");
            case LOGIN -> emailI18nUtils.getMessage("email.subject.login");
            default -> emailI18nUtils.getMessage("email.subject.default");
        };
    }

    private String buildEmailContent(VerificationEmailRequest.Purpose purpose, String code) {
        Context context = new Context();
        String action = getActionByPurpose(purpose);

        // 使用国际化消息
        String greeting = emailI18nUtils.getMessage("email.greeting");
        String descriptionTemplate = emailI18nUtils.getMessage("email.description");
        String description = String.format(descriptionTemplate, action);

        context.setVariable("greeting", greeting);
        context.setVariable("description", description);
        context.setVariable("code", code);
        context.setVariable("action", action);

        // 使用国际化的模板变量
        context.setVariable("companyName", emailI18nUtils.getMessage("email.company.name"));
        context.setVariable("companyTagline", emailI18nUtils.getMessage("email.company.tagline"));
        context.setVariable("validity", emailI18nUtils.getMessage("email.validity"));
        context.setVariable("teamName", emailI18nUtils.getMessage("email.team.name"));

        // 添加模板国际化变量
        context.setVariable("emailTitle", emailI18nUtils.getMessage("email.title"));
        context.setVariable("codeLabel", emailI18nUtils.getMessage("email.code.label"));
        context.setVariable("securityTitle", emailI18nUtils.getMessage("email.security.title"));
        context.setVariable("securityContent", emailI18nUtils.getMessage("email.security.content"));
        context.setVariable("signatureThanks", emailI18nUtils.getMessage("email.signature.thanks"));
        context.setVariable("footerSlogan", emailI18nUtils.getMessage("email.footer.slogan"));

        return emailTemplateEngine.process("email-template", context);
    }

    private String getActionByPurpose(VerificationEmailRequest.Purpose purpose) {
        return switch (purpose) {
//            case REGISTER -> emailI18nUtils.getMessage("email.action.register");
            case RESET_PASSWORD -> emailI18nUtils.getMessage("email.action.reset.password");
            case CHANGE_EMAIL -> emailI18nUtils.getMessage("email.action.change.email");
            case LOGIN -> emailI18nUtils.getMessage("email.action.login");
            default -> emailI18nUtils.getMessage("email.action.default");
        };
    }
}