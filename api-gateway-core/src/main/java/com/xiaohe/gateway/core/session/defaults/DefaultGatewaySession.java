package com.xiaohe.gateway.core.session.defaults;

import com.xiaohe.gateway.core.bind.IGenericReference;
import com.xiaohe.gateway.core.executor.Executor;
import com.xiaohe.gateway.core.mapping.HttpStatement;
import com.xiaohe.gateway.core.session.Configuration;
import com.xiaohe.gateway.core.session.GatewaySession;

import java.util.Map;

public class DefaultGatewaySession implements GatewaySession {

    private Configuration configuration;

    private String uri;

    private Executor executor;

    public DefaultGatewaySession(Configuration configuration, String uri, Executor executor) {
        this.configuration = configuration;
        this.uri = uri;
        this.executor = executor;
    }

    /**
     * 根据全限定方法名、参数执行对应的方法，并返回结果
     * @param methodName
     * @param params
     * @return
     */
    @Override
    public Object get(String methodName, Map<String, Object> params) {
        // 1. 解析请求
        HttpStatement httpStatement = configuration.getHttpStatement(uri);
        try {
            // 2. 执行请求
            return executor.exec(httpStatement, params);
        } catch (Exception e) {
            throw new RuntimeException("Error exec get. Cause : " + e);
        }
    }

    @Override
    public Object post(String methodName, Map<String, Object> params) {
        return get(methodName, params);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public IGenericReference getMapper() {
        return configuration.getMapper(uri, this);
    }
}
