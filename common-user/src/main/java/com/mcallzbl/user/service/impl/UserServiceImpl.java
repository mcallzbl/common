package com.mcallzbl.user.service.impl;

import com.mcallzbl.common.BusinessException;
import com.mcallzbl.common.enums.DeleteStatus;
import com.mcallzbl.user.enums.Gender;
import com.mcallzbl.user.enums.UserStatus;
import com.mcallzbl.user.mapper.UserMapper;
import com.mcallzbl.user.pojo.entity.User;
import com.mcallzbl.user.service.UserService;
import com.mcallzbl.user.utils.UsernameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 用户服务实现类
 * 提供用户查询、创建和验证功能
 *
 * @author mcallzbl
 * @version 2.0
 * @since 2025/10/26
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    // ==================== 直接查询方法（可能返回null） ====================

    /**
     * 直接根据用户ID查询用户（不验证状态）
     *
     * @param userId 用户ID
     * @return 用户信息，可能为null
     */
    @Override
    public User findUserById(Long userId) {
        return userMapper.selectByUserId(userId);
    }

    /**
     * 直接根据邮箱查询用户（不验证状态）
     *
     * @param email 邮箱
     * @return 用户信息，可能为null
     */
    @Override
    public User findUserByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    /**
     * 直接根据用户名查询用户（不验证状态）
     *
     * @param username 用户名
     * @return 用户信息，可能为null
     */
    @Override
    public User findUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    // ==================== 验证查询方法（确保用户可用） ====================

    /**
     * 获取用户并验证其可用性
     *
     * @param userId 用户ID
     * @return 用户信息，保证用户存在且可用
     * @throws BusinessException 如果用户不存在或不可用
     */
    @Override
    public User getUserById(Long userId) {
        User user = findUserById(userId);
        validateUserAvailable(user, "用户ID", userId.toString());
        return user;
    }

    /**
     * 获取用户并验证其可用性
     *
     * @param email 邮箱
     * @return 用户信息，保证用户存在且可用
     * @throws BusinessException 如果用户不存在或不可用
     */
    @Override
    public User getUserByEmail(String email) {
        User user = findUserByEmail(email);
        validateUserAvailable(user, "邮箱", email);
        return user;
    }

    /**
     * 获取用户并验证其可用性
     *
     * @param username 用户名
     * @return 用户信息，保证用户存在且可用
     * @throws BusinessException 如果用户不存在或不可用
     */
    @Override
    public User getUserByUsername(String username) {
        User user = findUserByUsername(username);
        validateUserAvailable(user, "用户名", username);
        return user;
    }

    // ==================== 私有验证方法 ====================

    /**
     * 验证用户是否可用
     *
     * @param user       用户对象
     * @param fieldType  字段类型（用于错误信息）
     * @param fieldValue 字段值（用于错误信息）
     * @throws BusinessException 如果用户不可用
     */
    private void validateUserAvailable(User user, String fieldType, String fieldValue) {
        if (user == null) {
            throw BusinessException.of(fieldType + "不存在");
        }

        if (user.isInActive()) {
            throw BusinessException.of("用户已被禁用");
        }

        if (user.getDeleteStatus().isDeleted()) {
            throw BusinessException.of("用户已被删除");
        }
    }

    /**
     * 创建新用户 默认就是邮件已经被验证好的
     *
     * @param email 邮箱
     * @return 创建的用户信息
     */
    @Override
    public User createUserByEmail(String email) {
        User user = User.builder()
                .email(email)
                .nickname(generateUniqueUsername(UsernameGenerator.generateUsernameFromEmail(email)))
                .username(generateUniqueUsername(UsernameGenerator.generateUsernameFromEmail(email)))
                .emailVerified(true)
                .gender(Gender.UNKNOWN)
                .status(UserStatus.NORMAL)
                .deleteStatus(DeleteStatus.NORMAL)
                .build();
        userMapper.insert(user);
        return user;
    }

    /**
     * 插入新用户到数据库
     *
     * @param user 要插入的用户实体
     * @return 插入是否成功
     */
    @Override
    public boolean insertUser(User user) {
        int insertCount = userMapper.insert(user);
        return insertCount > 0;
    }

    /**
     * 通过id更新用户信息
     *
     * @param user 要更新的用户实体
     * @return 更新是否成功
     */
    @Override
    public boolean updateUser(User user) {
        int updateCount = userMapper.updateById(user);
        return updateCount > 0;
    }

    /**
     * 生成唯一的用户名
     * 检查用户名是否已存在，如果存在则添加随机后缀重新生成
     *
     * @param baseUsername 基础用户名
     * @return 唯一的用户名
     */
    private String generateUniqueUsername(String baseUsername) {
        // 检查基础用户名是否已存在
        if (userMapper.selectByUsername(baseUsername) == null) {
            return baseUsername;
        }

        // 如果已存在，添加随机后缀
        int suffix = 1;
        String uniqueUsername;

        do {
            uniqueUsername = baseUsername + "_" + suffix;
            suffix++;

            // 防止无限循环，最多尝试100次
            if (suffix > 100) {
                log.warn("用户名生成尝试次数过多，使用完全随机的用户名");
                return generateUniqueUsername(UsernameGenerator.generateRandomUsername());
            }

        } while (userMapper.selectByUsername(uniqueUsername) != null);

        log.debug("用户名 {} 已存在，使用 {} 代替", baseUsername, uniqueUsername);
        return uniqueUsername;
    }
}