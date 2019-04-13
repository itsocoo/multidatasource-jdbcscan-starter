package com.itsocoo.multidatasource.jdbc.scan.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 方法参数中注入当前使用的DataSource
 * @date 2019/4/12 10:18
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiChoiceParams {
    /**
     * Alias for {@link #name}.
     */
    @AliasFor("name")
    String value() default "";

    /**
     * The name of the path variable to bind to.
     *
     * @since 4.3.3
     */
    @AliasFor("value")
    String name() default "";
}
