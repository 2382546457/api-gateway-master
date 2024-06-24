package com.xiaohe.gateway.sdk.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiProducerMethod {
    /**
     * 方法名称
     * @return
     */
    String methodName() default "";

    /**
     * 访问路径
     * @return
     */
    String uri() default "";

    /**
     * 请求type
     * @return
     */
    String httpCommandType() default "GET";

    /**
     * 是否需要认证 0-false 1-true
     * @return
     */
    int auth() default 0;
}
