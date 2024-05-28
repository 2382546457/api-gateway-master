package com.xiaohe.gateway.core.bind;

import com.xiaohe.gateway.core.executor.result.SessionResult;

import java.util.Map;

/**
 * 统一泛化调用接口
 */
public interface IGenericReference {
    SessionResult $invoke(Map<String, Object> params);
}
