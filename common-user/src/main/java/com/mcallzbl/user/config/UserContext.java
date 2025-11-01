package com.mcallzbl.user.config;

import com.mcallzbl.user.pojo.entity.User;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
public class UserContext {

    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    /**
     * 设置当前用户
     *
     * @param user 用户信息
     */
    public static void setCurrentUser(User user) {
        userHolder.set(user);
    }

    /**
     * 获取当前用户
     *
     * @return 用户信息，如果未登录返回null
     */
    public static User getCurrentUser() {
        return userHolder.get();
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID，如果未登录返回null
     */
    public static Long getCurrentUserId() {
        // TODO 这里直接抛出异常
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }


    /**
     * 判断是否已登录
     *
     * @return true-已登录，false-未登录
     */
    public static boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    /**
     * 清除当前用户信息
     */
    public static void clear() {
        userHolder.remove();
    }

}