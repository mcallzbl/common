package com.mcallzbl.user.pojo.vo;

import com.mcallzbl.user.enums.Gender;
import com.mcallzbl.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

/**
 * 用户信息视图对象
 *
 * @author mcallzbl
 * @since 2025/11/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户信息视图对象")
public class UserVO {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1001")
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    /**
     * 邮箱地址
     */
    @Schema(description = "邮箱地址", example = "zhangsan@example.com")
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码", example = "13812345678")
    private String phone;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    /**
     * 头像URL地址
     */
    @Schema(description = "头像URL地址", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    /**
     * 性别
     */
    @Schema(description = "性别：0-未知，1-男，2-女", example = "1")
    private Gender gender;

    /**
     * 生日
     */
    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;

    /**
     * 用户状态
     */
    @Schema(description = "用户状态：0-禁用，1-正常，2-冻结", example = "1")
    private UserStatus status;

    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间", example = "2025-11-17T10:30:00Z")
    private Instant lastLoginTime;

    /**
     * 最后登录IP地址
     */
    @Schema(description = "最后登录IP地址", example = "192.168.1.100")
    private String lastLoginIp;

    /**
     * 登录次数统计
     */
    @Schema(description = "登录次数统计", example = "15")
    private Integer loginCount;

    /**
     * 邮箱是否已验证
     */
    @Schema(description = "邮箱是否已验证", example = "true")
    private Boolean emailVerified;

    /**
     * 手机是否已验证
     */
    @Schema(description = "手机是否已验证", example = "false")
    private Boolean phoneVerified;

    /**
     * 用户时区设置
     */
    @Schema(description = "用户时区设置", example = "Asia/Shanghai")
    private String timezone;

    /**
     * 用户语言偏好
     */
    @Schema(description = "用户语言偏好", example = "zh-CN")
    private String language;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2025-10-25T08:00:00Z")
    private Instant createdTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2025-11-17T10:30:00Z")
    private Instant updatedTime;

}