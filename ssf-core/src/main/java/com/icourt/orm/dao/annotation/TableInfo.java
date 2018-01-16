package com.icourt.orm.dao.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标注表名注解
 * @author june
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface TableInfo {

    String tableName();
    String pkName() default "";//多个逗号隔开
}
