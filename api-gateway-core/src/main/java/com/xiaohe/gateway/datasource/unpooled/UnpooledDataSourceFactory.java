package com.xiaohe.gateway.datasource.unpooled;

import com.xiaohe.gateway.datasource.DataSource;
import com.xiaohe.gateway.datasource.DataSourceFactory;
import com.xiaohe.gateway.datasource.DataSourceType;
import com.xiaohe.gateway.session.Configuration;

public class UnpooledDataSourceFactory implements DataSourceFactory {
    protected UnpooledDataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public void setProperties(Configuration configuration, String uri) {
        this.dataSource.setConfiguration(configuration);
        this.dataSource.setDataSourceType(DataSourceType.Dubbo);
        this.dataSource.setHttpStatement(configuration.getHttpStatement(uri));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
