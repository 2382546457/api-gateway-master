package com.xiaohe.gateway.session.defaults;

import com.xiaohe.gateway.session.Configuration;
import com.xiaohe.gateway.session.GatewaySession;
import com.xiaohe.gateway.session.GatewaySessionFactory;

public class DefaultGatewaySessionFactory implements GatewaySessionFactory {
    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession() {
        return new DefaultGatewaySession(configuration);
    }
}
