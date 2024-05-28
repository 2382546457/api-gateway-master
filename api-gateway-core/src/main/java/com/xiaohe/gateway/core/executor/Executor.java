package com.xiaohe.gateway.core.executor;

import com.xiaohe.gateway.core.executor.result.SessionResult;
import com.xiaohe.gateway.core.mapping.HttpStatement;

import java.util.Map;

/**
 * 执行器
 */
public interface Executor {
    SessionResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception;
}
