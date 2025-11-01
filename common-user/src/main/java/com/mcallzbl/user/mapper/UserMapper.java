package com.mcallzbl.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcallzbl.user.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/10/26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    default User selectByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return selectOne(queryWrapper);
    }

    default User selectByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return selectOne(queryWrapper);
    }

    default User selectByPhone(String phone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        return selectOne(queryWrapper);
    }

    default User selectByUserId(long userId) {
        return selectById(userId);
    }
}
