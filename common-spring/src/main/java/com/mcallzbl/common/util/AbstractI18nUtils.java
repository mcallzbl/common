package com.mcallzbl.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
@Slf4j
@Component
public abstract class AbstractI18nUtils {
    private final MessageSource messageSource;

    public AbstractI18nUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 获取国际化消息（使用当前请求的Locale）
     *
     * @param code 消息代码
     * @return 国际化消息
     */
    public String getMessage(String code) {
        return getMessage(code, LocaleContextHolder.getLocale());
    }

    /**
     * 获取国际化消息（使用当前请求的Locale，带参数）
     *
     * @param code 消息代码
     * @param args 参数数组
     * @return 国际化消息
     */
    public String getMessage(String code, Object... args) {
        return getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * 获取国际化消息（指定Locale）
     *
     * @param code   消息代码
     * @param locale 指定的Locale
     * @return 国际化消息
     */
    public String getMessage(String code, Locale locale) {
        try {
            return messageSource.getMessage(code, null, locale);
        } catch (NoSuchMessageException e) {
            log.warn("Failed to get message for code: {}, locale: {}", code, locale, e);
            return code; // 如果找不到消息，返回code本身
        }
    }

    /**
     * 获取国际化消息（指定Locale，带参数）
     *
     * @param code   消息代码
     * @param args   参数数组
     * @param locale 指定的Locale
     * @return 国际化消息
     */
    public String getMessage(String code, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            log.warn("Failed to get message for code: {}, locale: {}, args: {}", code, locale, args, e);
            return code; // 如果找不到消息，返回code本身
        }
    }

    /**
     * 获取国际化消息（指定语言，如 "zh_CN", "en"）
     *
     * @param code     消息代码
     * @param language 语言代码
     * @return 国际化消息
     */
    public String getMessage(String code, String language) {
        Locale locale = parseLocale(language);
        return getMessage(code, locale);
    }

    /**
     * 获取国际化消息（指定语言，带参数）
     *
     * @param code     消息代码
     * @param language 语言代码
     * @param args     参数数组
     * @return 国际化消息
     */
    public String getMessage(String code, String language, Object... args) {
        Locale locale = parseLocale(language);
        return getMessage(code, args, locale);
    }

    /**
     * 解析语言代码为Locale对象
     *
     * @param language 语言代码（如 "zh_CN", "en", "ja_JP"）
     * @return Locale对象
     */
    private Locale parseLocale(String language) {
        if (!StringUtils.hasText(language)) {
            return Locale.getDefault();
        }
        try {
            String[] parts = language.split("_");
            if (parts.length == 1) {
                return new Locale(parts[0]);
            } else if (parts.length == 2) {
                return new Locale(parts[0], parts[1]);
            } else {
                return new Locale(parts[0], parts[1], parts[2]);
            }
        } catch (Exception e) {
            log.warn("Failed to parse locale: {}", language, e);
            return Locale.getDefault();
        }
    }

    /**
     * 获取当前请求的Locale
     *
     * @return 当前Locale
     */
    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * 判断是否为中文环境
     *
     * @return true if current locale is Chinese
     */
    public boolean isChineseLocale() {
        Locale locale = getCurrentLocale();
        return Locale.CHINESE.getLanguage().equals(locale.getLanguage());
    }

    /**
     * 判断是否为日文环境
     *
     * @return true if current locale is Japanese
     */
    public boolean isJapaneseLocale() {
        Locale locale = getCurrentLocale();
        return Locale.JAPANESE.getLanguage().equals(locale.getLanguage());
    }

    /**
     * 判断是否为英文环境
     *
     * @return true if current locale is English
     */
    public boolean isEnglishLocale() {
        Locale locale = getCurrentLocale();
        return Locale.ENGLISH.getLanguage().equals(locale.getLanguage());
    }
}
