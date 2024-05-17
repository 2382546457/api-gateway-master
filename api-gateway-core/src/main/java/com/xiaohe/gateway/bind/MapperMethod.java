package com.xiaohe.gateway.bind;


import com.xiaohe.gateway.mapping.HttpCommandType;
import com.xiaohe.gateway.session.Configuration;
import com.xiaohe.gateway.session.GatewaySession;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 绑定调用方法
 * 以后使用 MapperMethod.execute() 就可以执行并获取结果
 */
public class MapperMethod {
    /**
     * 全限定方法名
     */
    private String methodName;
    private final HttpCommandType command;

    public MapperMethod(String uri, Method method, Configuration configuration) {
        this.methodName = configuration.getHttpStatement(uri).getMethodName();
        this.command = configuration.getHttpStatement(uri).getHttpCommandType();
    }

    public Object execute(GatewaySession session, Map<String, Object> args) {
        Object result = null;
        switch (command) {
            case GET:
                result = session.get(methodName, args);
                break;
            case POST:
                break;
            case PUT:
                break;
            case DELETE:
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + command);
        }
        return result;
    }
}
