package com.sys.annotation;

import java.lang.annotation.*;

/**
 * @author zzl
 *         Date:2014-10-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
@Documented
public @interface Ehcache {
    String cacheName() default "";

    // 增加缓存还是删除缓存，默认为增加缓存
    boolean addOrdel() default true;

}
