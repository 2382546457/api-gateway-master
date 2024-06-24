package com.xiaohe.gateway.sdk.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ApiProducerClazz {

    /**
     * 接口名称
     * @return
     */
    String interfaceName() default "";

    /**
     * 接口版本
     * @return
     */
    String interfaceVersion() default "";
}
