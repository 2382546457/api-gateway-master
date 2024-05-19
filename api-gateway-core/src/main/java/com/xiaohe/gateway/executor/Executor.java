package com.xiaohe.gateway.executor;

import com.xiaohe.gateway.executor.result.SessionResult;
import com.xiaohe.gateway.mapping.HttpStatement;

import java.util.Map;

/**
 * 执行器
 */
public interface Executor {
    SessionResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception;
}
