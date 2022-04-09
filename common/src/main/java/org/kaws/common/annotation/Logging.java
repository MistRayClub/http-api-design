package org.kaws.common.annotation;

import java.lang.annotation.*;

/**
 * @author Bosco
 * @date 2022/4/9 2:54 下午
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Logging {

    String value() default "暂无标题";

    String title() default "暂无标题";
    
    String describe() default "暂无介绍";

}
