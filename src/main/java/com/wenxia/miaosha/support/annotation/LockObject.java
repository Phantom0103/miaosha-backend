package com.wenxia.miaosha.support.annotation;

import java.lang.annotation.*;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-13
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockObject {
}
