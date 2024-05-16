package com.xiaohe.gateway.session.defaults;

import com.xiaohe.gateway.bind.IGenericReference;
import com.xiaohe.gateway.datasource.Connection;
import com.xiaohe.gateway.datasource.DataSource;
import com.xiaohe.gateway.session.Configuration;
import com.xiaohe.gateway.session.GatewaySession;

public class DefaultGatewaySession implements GatewaySession {

    private Configuration configuration;

    private String uri;

    private DataSource dataSource;

    public DefaultGatewaySession(Configuration configuration, String uri, DataSource dataSource) {
        this.configuration = configuration;
        this.uri = uri;
        this.dataSource = dataSource;
    }

    /**
     * 根据全限定方法名、参数执行对应的方法，并返回结果
     * @param methodName
     * @param parameter
     * @return
     */
    @Override
    public Object get(String methodName, Object parameter) {
        Connection connection = dataSource.getConnection();
        return connection.execute(methodName, new String[]{"java.land.String"}, new String[]{"name"}, new Object[]{parameter});
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
