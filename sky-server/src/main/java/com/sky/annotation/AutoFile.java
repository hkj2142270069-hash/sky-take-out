package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 */
@Target( ElementType.METHOD)// 表示该注解只能用于方法
@Retention(RetentionPolicy.RUNTIME) // 表示注解保留到运行时
public @interface AutoFile {
    /**
     * 操作类型 update insert
     * @return
     */
    OperationType value();
}
