package com.xiaohe.gateway.executor;

import com.xiaohe.gateway.datasource.Connection;
import com.xiaohe.gateway.session.Configuration;

public class SimpleExecutor extends BaseExecutor {

    public SimpleExecutor(Configuration configuration, Connection connection) {
        super(configuration, connection);
    }

    @Override
    protected Object doExec(String methodName, String[] parameterTypes, Object[] args) {
        return connection.execute(methodName, parameterTypes, new String[]{"ignore"}, args);
    }
}
