package com.mcallzbl.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mcallzbl.common.BaseEntity;
import com.mcallzbl.user.enums.RoleStatus;
import lombok.*;

import java.time.Instant;

/**
 * 角色信息实体类
 *
 * @author mcallzbl
 * @since 2025-11-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("role")
public class Role extends BaseEntity {

    /**
     * 角色ID，主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 角色名称
     */
    @TableField(value = "role_name")
    private String roleName;

    /**
     * 角色编码
     */
    @TableField(value = "role_code")
    private String roleCode;

    /**
     * 角色描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField(value = "status")
    private RoleStatus status = RoleStatus.ENABLED;

}