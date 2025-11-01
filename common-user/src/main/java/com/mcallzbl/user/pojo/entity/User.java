package com.mcallzbl.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mcallzbl.common.BaseEntity;
import com.mcallzbl.common.enums.DeleteStatus;
import com.mcallzbl.user.enums.Gender;
import com.mcallzbl.user.enums.UserStatus;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

/**
 * 用户信息实体类
 *
 * @author mcallzbl
 * @since 2025-10-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user")
public class User extends BaseEntity {

    /**
     * 用户ID，主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户名，登录用
     */
    @TableField(value = "username")
    private String username;

    /**
     * 邮箱地址，用于登录和通知
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机号码，可选
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 密码，加密存储
     */
    @TableField(value = "password_hash")
    private String passwordHash;

    /**
     * 用户昵称，显示名称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 头像URL地址
     */
    @TableField(value = "avatar_url")
    private String avatarUrl;

    /**
     * 性别：0-未知，1-男，2-女
     */
    @TableField(value = "gender")
    private Gender gender = Gender.UNKNOWN;

    /**
     * 生日
     */
    @TableField(value = "birthday")
    private LocalDate birthday;

    /**
     * 用户状态：0-禁用，1-正常，2-冻结
     */
    @TableField(value = "status")
    private UserStatus status = UserStatus.NORMAL;

    /**
     * 最后登录时间
     */
    @TableField(value = "last_login_time")
    private Instant lastLoginTime;

    /**
     * 最后登录IP地址
     */
    @TableField(value = "last_login_ip")
    private String lastLoginIp;

    /**
     * 登录次数统计
     */
    @TableField(value = "login_count")
    private Integer loginCount = 0;

    /**
     * 邮箱是否已验证：0-未验证，1-已验证
     */
    @TableField(value = "email_verified")
    private Boolean emailVerified = false;

    /**
     * 手机是否已验证：0-未验证，1-已验证
     */
    @TableField(value = "phone_verified")
    private Integer phoneVerified = 0;

    /**
     * 用户时区设置
     */
    @TableField(value = "timezone")
    private String timezone = "Asia/Shanghai";

    /**
     * 用户语言偏好
     */
    @TableField(value = "language")
    private String language = "zh-CN";

    /**
     * 是否删除：0-正常，1-已删除
     */
    @TableField(value = "is_deleted")
    private DeleteStatus deleteStatus = DeleteStatus.DELETED;

    /**
     * 删除时间，NULL表示未删除
     */
    @TableField(value = "deleted_time")
    private Instant deletedTime;

    /**
     * 删除原因
     */
    @TableField(value = "deleted_reason")
    private String deletedReason;

    /**
     * 创建时间
     */
//    @TableField(value = "created_time", fill = FieldFill.INSERT)
//    private Instant createdTime;
//
//    /**
//     * 更新时间
//     */
//    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
//    private Instant updatedTime;

    /**
     * 软删除用户
     */
    public void softDelete(String reason) {
        this.deleteStatus = DeleteStatus.DELETED;
        this.deletedTime = Instant.now();
        this.deletedReason = reason;
    }

    /**
     * 检查用户是否正常状态
     */
    public boolean isInActive() {
        return status != UserStatus.NORMAL || deleteStatus.isDeleted();
    }

    /**
     * 更新登录信息
     */
    public void updateLoginInfo(String loginIp) {
        this.lastLoginTime = Instant.now();
        this.lastLoginIp = loginIp;
        this.loginCount = (this.loginCount == null ? 0 : this.loginCount) + 1;
    }

}