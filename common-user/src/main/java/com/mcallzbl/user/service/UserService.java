package com.mcallzbl.user.service;

import com.mcallzbl.user.pojo.entity.User;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
public interface UserService {
    /**
     * 根据用户ID获取用户
     *
     * @param userId 用户ID
     * @return 用户信息，如果用户不存在或状态不为1则返回null
     */
    User getUserById(Long userId);

    /**
     * 根据邮箱获取用户
     *
     * @param email 邮箱
     * @return 用户信息，如果用户不存在或状态不可用则返回null
     */
    User getUserByEmail(String email);

    /**
     * 创建新用户
     *
     * @param email 邮箱
     * @return 创建的用户信息
     */
    User createUserByEmail(String email);
}