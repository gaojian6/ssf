package com.icourt.orm.dao.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 不需要对应数据库字段的可添加这个注解标识
 * @author june
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Transient {
}
