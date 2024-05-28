package com.xiaohe.gateway.core.session;

/**
 * 创建 session 的工厂
 */
public interface GatewaySessionFactory {
    public GatewaySession openSession(String uri);
}
