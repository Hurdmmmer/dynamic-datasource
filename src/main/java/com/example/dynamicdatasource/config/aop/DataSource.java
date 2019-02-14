package com.example.dynamicdatasource.config.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
public @interface DataSource {
    /** 指定数据库的名称，默认是 master 数据库 */
    String name() default "master";
}
