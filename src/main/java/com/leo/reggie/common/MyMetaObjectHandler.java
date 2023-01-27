package com.leo.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.leo.reggie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("auto fill when inserted ");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        Long currentId = BaseContext.getCurrentId();
        metaObject.setValue("createUser", currentId);
        metaObject.setValue("updateUser", currentId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("auto fill when updated");
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long currentId = BaseContext.getCurrentId();
        metaObject.setValue("updateUser", currentId);


    }
}
