package com.itsocoo.multidatasource.jdbc.scan.annotations;

import java.lang.annotation.*;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 获取方法的相关信息
 * @date 2018/8/31 14:42
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiDataScanApiService {
    String name() default "";
}
