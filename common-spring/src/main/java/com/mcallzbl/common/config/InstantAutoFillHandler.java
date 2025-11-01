package com.mcallzbl.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @author mcallzbl
 * @version 1.0
 * @since 2025/11/1
 */
@Component
@Slf4j
public class InstantAutoFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Instant now = Instant.now();
        log.info("自动填充插入时间: {}", now);
        metaObject.setValue("createdTime", now);
        metaObject.setValue("updatedTime", now);
//        this.strictInsertFill(metaObject, "createdTime", Instant.class, now);
//        this.strictInsertFill(metaObject, "updateTime", Instant.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Instant now = Instant.now();
        log.info("自动填充更新时间: {}", now);
        metaObject.setValue("updatedTime", now);
//        fillStrategy(metaObject, "updatedTime", now);
//        this.strictUpdateFill(metaObject, "updatedTime", Instant.class, now);
        log.info("自动填充后 - 元对象中的updateTime: {}", metaObject.getValue("updatedTime"));
    }
}
