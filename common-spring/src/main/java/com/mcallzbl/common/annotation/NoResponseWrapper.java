package com.mcallzbl.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 禁用响应包装注解
 * 标记在Controller类或方法上，表示该Controller或方法的返回值不需要被统一包装
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoResponseWrapper {
}