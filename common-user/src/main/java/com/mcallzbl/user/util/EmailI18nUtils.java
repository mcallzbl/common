package com.mcallzbl.user.util;

import com.mcallzbl.common.util.AbstractI18nUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 邮件国际化工具类
 * 专门处理邮件相关的国际化消息，使用独立的邮件消息源
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Slf4j
@Component
public class EmailI18nUtils extends AbstractI18nUtils {

    @Autowired
    public EmailI18nUtils(@Qualifier("emailMessageSource") MessageSource messageSource) {
        super(messageSource);
    }

}