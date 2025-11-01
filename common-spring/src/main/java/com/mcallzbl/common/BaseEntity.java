package com.mcallzbl.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.Instant;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
@Data
public abstract class BaseEntity {
    @TableField(fill = FieldFill.INSERT)
    private Instant createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Instant updatedTime;
}
