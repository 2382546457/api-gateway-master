package com.xiaohe.gateway.session.defaults;

import com.xiaohe.gateway.datasource.DataSource;
import com.xiaohe.gateway.datasource.DataSourceFactory;
import com.xiaohe.gateway.datasource.unpooled.UnpooledDataSourceFactory;
import com.xiaohe.gateway.executor.Executor;
import com.xiaohe.gateway.session.Configuration;
import com.xiaohe.gateway.session.GatewaySession;
import com.xiaohe.gateway.session.GatewaySessionFactory;

public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 给一个 uri 创建一个 session
     * @param uri
     * @return
     */
    @Override
    public GatewaySession openSession(String uri) {
        UnpooledDataSourceFactory dataSourceFactory = new UnpooledDataSourceFactory();
        dataSourceFactory.setProperties(configuration, uri);
        DataSource dataSource = dataSourceFactory.getDataSource();
        // 创建执行器
        Executor executor = configuration.newExecutor(dataSource.getConnection());
        return new DefaultGatewaySession(configuration, uri, executor);
    }
}
