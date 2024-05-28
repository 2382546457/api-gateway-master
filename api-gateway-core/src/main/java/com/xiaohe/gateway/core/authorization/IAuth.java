package com.xiaohe.gateway.core.authorization;


/**
 * 认证服务接口
 */
public interface IAuth {

    boolean validate(String id, String token);

}