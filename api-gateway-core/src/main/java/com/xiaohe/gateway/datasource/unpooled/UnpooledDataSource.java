package com.xiaohe.gateway.datasource.unpooled;

import com.xiaohe.gateway.datasource.Connection;
import com.xiaohe.gateway.datasource.DataSource;
import com.xiaohe.gateway.datasource.DataSourceType;
import com.xiaohe.gateway.datasource.connection.DubboConnection;
import com.xiaohe.gateway.mapping.HttpStatement;
import com.xiaohe.gateway.session.Configuration;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * 无池化的连接池
 */
public class UnpooledDataSource implements DataSource {

    private Configuration configuration;

    private HttpStatement httpStatement;
    /**
     * 生产的连接的类型，支持 HTTP、Dubbo
     */
    private DataSourceType dataSourceType;
    @Override
    public Connection getConnection() {
        switch (dataSourceType) {
            case HTTP:
                // TODO 暂时不实现
                break;
            case Dubbo:
                // 获取配置信息
                String application = httpStatement.getApplication();
                String interfaceName = httpStatement.getInterfaceName();
                // 获取对应的服务
                ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
                RegistryConfig registryConfig = configuration.getRegistryConfig(application);
                ReferenceConfig<GenericService> reference = configuration.getReferenceConfig(interfaceName);
                return new DubboConnection(applicationConfig, registryConfig, reference);
            default:
                break;
        }
        throw new RuntimeException("DataSourceType: " + dataSourceType + "没有对应的数据源实现");
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setHttpStatement(HttpStatement httpStatement) {
        this.httpStatement = httpStatement;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }
}
