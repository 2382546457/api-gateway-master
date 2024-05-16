package com.xiaohe.gateway.bind;

import com.xiaohe.gateway.mapping.HttpStatement;
import com.xiaohe.gateway.session.Configuration;
import com.xiaohe.gateway.session.GatewaySession;

import java.util.HashMap;
import java.util.Map;

/**
 * 泛化调用的注册器，主要是将 uri 与 MapperProxyFactory 做映射
 */
public class MapperRegistry {
    private final Configuration configuration;

    /**
     * uri - MapperProxyFactory
     */
    private final Map<String, MapperProxyFactory> knownMappers = new HashMap<>();

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 根据 uri 创建对应的代理对象，将其转为 IGenericReference 返回
     * @param uri
     * @param gatewaySession
     * @return
     */
    public IGenericReference getMapper(String uri, GatewaySession gatewaySession) {
        MapperProxyFactory mapperProxyFactory = knownMappers.get(uri);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("没有 uri 对应的 MapperProxyFactory, 请先注册");
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
            throw new RuntimeException("请不要重复注册 MapperProxyFactory, uri " + uri);
        }
        // 放到Map中
        knownMappers.put(uri, new MapperProxyFactory(uri));
        // 外部调用时，通过 Configuration 判断这个服务是否可以调用，configuration 中拥有 mapperRegistry
        configuration.addHttpStatement(httpStatement);
    }

    /**
     * 对应 uri 的代理工厂是否已经注册
     * @param uri
     * @return
     * @param <T>
     */
    public <T> boolean hasMapper(String uri) {
        return knownMappers.containsKey(uri);
    }
}
