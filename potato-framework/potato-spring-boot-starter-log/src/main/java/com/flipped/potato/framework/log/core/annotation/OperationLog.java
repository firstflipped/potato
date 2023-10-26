package com.flipped.potato.framework.log.core.annotation;

import com.flipped.potato.framework.log.core.entity.emnus.OperationTypeEnum;

import java.lang.annotation.*;

/**
 * 自定义注解类
 * 平台日志记录注解
 *
 * <p>@Target({METHOD,TYPE}) 表示这个注解可以用用在类/接口上，还可以用在方法上</p>
 * <p>@Retention(RetentionPolicy.RUNTIME) 表示这是一个运行时注解，即运行起来之后，才获取注解中的相关信息，
 * 而不像基本注解如@Override 那种不用运行，在编译时eclipse就可以进行相关工作的编译时注解。</p>
 * <p>@Inherited 表示这个注解可以被子类继承</p>
 * <p>@Documented 表示当执行javadoc的时候，本注解会生成相关文档</p>
 *
 * @author <a href="#">flipped</a>
 * @version v1.0
 * @since 2022-04-11 19:35:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 是否记录
     *
     * @return 是否记录编码
     */
    boolean record() default true;

    /**
     * 操作模块
     *
     * @return 操作模块名称
     */
    String module() default "";

    /**
     * 操作类型
     *
     * @return 操作类型
     */
    OperationTypeEnum type() default OperationTypeEnum.GET;

    /**
     * 操作描述
     *
     * @return 操作描述
     */
    String value() default "";

}
