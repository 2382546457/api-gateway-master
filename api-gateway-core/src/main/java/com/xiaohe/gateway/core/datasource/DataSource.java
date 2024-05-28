package com.xiaohe.gateway.core.datasource;


/**
 * 数据源接口，可以根据项目使用的RPC来选择不同的数据源，如 Dubbo、HTTP、Nacos..
 */
public interface DataSource {
    /**
     * 获取数据源
     * @return
     */
    Connection getConnection();
}
