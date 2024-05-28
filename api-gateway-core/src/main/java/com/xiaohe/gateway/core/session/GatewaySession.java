package com.xiaohe.gateway.core.session;


import com.xiaohe.gateway.core.bind.IGenericReference;

import java.util.Map;

/**
 * 每一个 session 都单独映射一个 uri
 */
public interface GatewaySession {

    Object get(String methodName, Map<String, Object> params);

    Object post(String methodName, Map<String, Object> params);

    Configuration getConfiguration();

    IGenericReference getMapper();
}
