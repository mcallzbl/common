package com.mcallzbl.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * 国际化工具类
 * 提供便捷的国际化消息获取方法
 *
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Slf4j
@Component
public class CommonI18nUtils extends AbstractI18nUtils {

    @Autowired
    public CommonI18nUtils(@Qualifier("commonMessageSource") MessageSource messageSource) {
        super(messageSource);
    }
}