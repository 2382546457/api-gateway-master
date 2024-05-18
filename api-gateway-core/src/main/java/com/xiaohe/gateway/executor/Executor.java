package com.xiaohe.gateway.executor;

import com.xiaohe.gateway.executor.result.GatewayResult;
import com.xiaohe.gateway.mapping.HttpStatement;

import java.util.Map;

/**
 * 执行器
 */
public interface Executor {
    GatewayResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception;
}
