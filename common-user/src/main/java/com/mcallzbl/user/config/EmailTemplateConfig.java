package com.mcallzbl.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.messageresolver.SpringMessageResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * 邮件模板引擎配置类
 * 为邮件模板配置独立的MessageSource，确保 #{email.xxx} 表达式能正确解析
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
@Slf4j
@Configuration
public class EmailTemplateConfig {

    /**
     * 创建邮件专用的模板引擎
     * 使用独立的MessageSource来解析邮件相关的国际化消息
     */
    @Bean("emailTemplateEngine")
    public TemplateEngine emailTemplateEngine(@Qualifier("emailMessageSource") MessageSource emailMessageSource) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        // 配置模板解析器
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setCacheable(true);

        templateEngine.setTemplateResolver(templateResolver);

        // 配置消息解析器 - 使用邮件专用的MessageSource
        SpringMessageResolver messageResolver = new SpringMessageResolver();
        messageResolver.setMessageSource(emailMessageSource);

        templateEngine.setMessageResolver(messageResolver);
        return templateEngine;
    }
}