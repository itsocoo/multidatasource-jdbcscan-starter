package com.itsocoo.multidatasource.jdbc.scan.annotations;

import com.itsocoo.multidatasource.jdbc.scan.enums.ExecuteType;
import org.springframework.transaction.annotation.Propagation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 获取方法的相关信息
 * @date 2018/8/31 14:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiDataScanApi {
    String sql() default "";

    // 执行类型
    ExecuteType executeType() default ExecuteType.SELECT;

    // 返回实体的Type
    Class<? extends Object> types() default Object.class;

    // 是否支持事务
    boolean transaction() default false;

    // 事务的传播类型
    Propagation propagation() default Propagation.REQUIRED;
}
