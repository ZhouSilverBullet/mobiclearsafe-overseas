package com.mobi.clearsafe.ui.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/7 10:52
 * @Dec 用来注释bug的名称
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,
        ElementType.TYPE,
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.CONSTRUCTOR,
        ElementType.LOCAL_VARIABLE,
        ElementType.ANNOTATION_TYPE,
        ElementType.TYPE_PARAMETER,
        ElementType.TYPE_USE,
        ElementType.PACKAGE})
public @interface Bugs {
    String value() default "";
    String[] arrValue() default {};
}
