package com.xiaohe.gateway.bind;

import com.xiaohe.gateway.mapping.HttpStatement;
import com.xiaohe.gateway.session.Configuration;
import com.xiaohe.gateway.session.GatewaySession;

import java.util.HashMap;
import java.util.Map;


/**
 * 泛化调用注册组件
 */
public class MapperRegistry {
    private final Configuration configuration;

    /**
     * key : uri
     * value : MapperProxyFactory
     */
    private final Map<String, MapperProxyFactory> knownMappers = new HashMap<>();

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public IGenericReference getMapper(String uri, GatewaySession gatewaySession) {
        final MapperProxyFactory mapperProxyFactory = knownMappers.get(uri);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Uri " + uri + " is not known to the MapperRegistry");
        }
        try {
            return mapperProxyFactory.newInstance(gatewaySession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public void addMapper(HttpStatement httpStatement) {
        String uri = httpStatement.getUri();
        if (hasMapper(uri)) {
            throw new RuntimeException("Uri " + uri + " is already known to the MapperRegistry.");
        }
        knownMappers.put(uri, new MapperProxyFactory(uri));
        configuration.addHttpStatement(httpStatement);
    }
    public <T> boolean hasMapper(String uri) {
        return knownMappers.containsKey(uri);
    }

}
