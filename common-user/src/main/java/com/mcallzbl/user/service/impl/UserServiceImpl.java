package com.mcallzbl.user.service.impl;

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
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    /**
     * 根据用户ID获取用户
     *
     * @param userId 用户ID
     * @return 用户信息，如果用户不存在或状态不为1则返回null
     */
    @Override
    public User getUserById(Long userId) {
        return userMapper.selectByUserId(userId);
    }

    /**
     * 根据邮箱获取用户
     *
     * @param email 邮箱
     * @return 用户信息，如果用户不存在或状态不可用则返回null
     */
    @Override
    public User getUserByEmail(String email) {
        return userMapper.selectByEmail(email);
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