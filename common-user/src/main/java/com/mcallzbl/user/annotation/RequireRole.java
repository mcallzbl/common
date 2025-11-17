package com.mcallzbl.user.annotation;


import com.mcallzbl.user.pojo.entity.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    String[] value(); // 所需角色数组
}

