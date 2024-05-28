package com.xiaohe.gateway.core.datasource;

import com.xiaohe.gateway.core.session.Configuration;

/**
 * 数据源工厂
 */
public interface DataSourceFactory {
    /**
     * 设置配置
     * @param configuration
     * @param uri
     */
    void setProperties(Configuration configuration, String uri);

    /**
     * 获取数据源
     * @return
     */
    DataSource getDataSource();
}
