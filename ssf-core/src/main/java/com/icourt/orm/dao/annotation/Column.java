package com.icourt.orm.dao.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author june
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Column {
    String name();//数据库字段名
}
