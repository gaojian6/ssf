package com.icourt.common.dao;

import java.lang.annotation.*;

/**
 * primary key mark
 *
 * @author lan
 * @since 1.0.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Id {

}
