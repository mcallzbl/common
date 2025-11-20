package com.mcallzbl.user.service;

import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.pojo.vo.UserVO;

/**
 * 用户服务接口
 * 提供用户查询、创建和验证功能
 *
 * @author mcallzbl
 * @version 2.0
 * @since 2025/10/26
 */
public interface UserService {

    // ==================== 直接查询方法（可能返回null） ====================

    /**
     * 直接根据用户ID查询用户（不验证状态）
     *
     * @param userId 用户ID
     * @return 用户信息，可能为null
     */
    User findUserById(Long userId);

    /**
     * 直接根据邮箱查询用户（不验证状态）
     *
     * @param email 邮箱
     * @return 用户信息，可能为null
     */
    User findUserByEmail(String email);

    /**
     * 直接根据用户名查询用户（不验证状态）
     *
     * @param username 用户名
     * @return 用户信息，可能为null
     */
    User findUserByUsername(String username);

    // ==================== 验证查询方法（确保用户可用） ====================

    /**
     * 获取用户并验证其可用性
     *
     * @param userId 用户ID
     * @return 用户信息，保证用户存在且可用
     * @throws com.mcallzbl.common.BusinessException 如果用户不存在或不可用
     */
    User getUserById(Long userId);

    /**
     * 获取用户并验证其可用性
     *
     * @param email 邮箱
     * @return 用户信息，保证用户存在且可用
     * @throws com.mcallzbl.common.BusinessException 如果用户不存在或不可用
     */
    User getUserByEmail(String email);

    /**
     * 获取用户并验证其可用性
     *
     * @param username 用户名
     * @return 用户信息，保证用户存在且可用
     * @throws com.mcallzbl.common.BusinessException 如果用户不存在或不可用
     */
    User getUserByUsername(String username);

    /**
     * 创建新用户
     *
     * @param email 邮箱
     * @return 创建的用户信息
     */
    User createUserByEmail(String email);

    /**
     * 插入新用户到数据库
     *
     * @param user 要插入的用户实体
     * @return 插入是否成功
     */
    boolean insertUser(User user);

    /**
     * 通过id更新用户信息
     *
     * @param user 要更新的用户实体
     * @return 更新是否成功
     */
    boolean updateUser(User user);

    /**
     * 获取当前登录用户信息并转换为VO
     *
     * @return 用户信息VO
     * @throws com.mcallzbl.common.BusinessException 如果用户未登录
     */
    UserVO getCurrentUserVO();
}