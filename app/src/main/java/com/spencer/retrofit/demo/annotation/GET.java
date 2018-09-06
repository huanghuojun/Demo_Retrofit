package com.spencer.retrofit.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author: huanghuojun
 * @date: 2018/9/6 15:38
 */
//给谁用
@Target(ElementType.METHOD)
//保留到什么时候
@Retention(RetentionPolicy.RUNTIME)
public @interface GET {
    String value();
}
