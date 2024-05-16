package com.xiaohe.gateway.session;


import com.xiaohe.gateway.bind.IGenericReference;

/**
 * 每一个 session 都单独映射一个 uri
 */
public interface GatewaySession {
    Object get(String methodName, Object parameter);

    Configuration getConfiguration();

    IGenericReference getMapper();
}
