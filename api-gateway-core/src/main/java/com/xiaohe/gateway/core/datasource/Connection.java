package com.xiaohe.gateway.core.datasource;

/**
 * 连接接口，由不同的服务实现。
 * 比如 gateway 管理的是 dubbo服务，那么就使用 DubboConnection，如果 gateway 管理的是Nacos、Http服务，就使用 NacosConnection、HttpConnection
 */
public interface Connection {
    /**
     * 执行方法，获取结果
     * @param method 方法名
     * @param parameterTypes 形参类型
     * @param parameterNames 形参名字
     * @param args 形参数据
     * @return
     */
    Object execute(String method, String[] parameterTypes, String[] parameterNames, Object[] args);
}
