package com.xiaohe.gateway.bind;

import com.xiaohe.gateway.mapping.HttpStatement;
import com.xiaohe.gateway.session.GatewaySession;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperProxyFactory {
    private String uri;

    /**
     * 存储 uri - IGenericReference 的映射, uri是HTTP请求的路径
     */
    private final Map<String, IGenericReference> genericReferenceCache = new ConcurrentHashMap<>();


    public MapperProxyFactory(String uri) {
        this.uri = uri;
    }


    public IGenericReference newInstance(GatewaySession gatewaySession) {
        return genericReferenceCache.computeIfAbsent(uri, k -> {
            HttpStatement httpStatement = gatewaySession.getConfiguration().getHttpStatement(uri);
            // 泛化调用
            MapperProxy genericReferenceProxy = new MapperProxy(gatewaySession, uri);
            // 创建接口
            InterfaceMaker interfaceMaker = new InterfaceMaker();
            interfaceMaker.add(new Signature(httpStatement.getMethodName(), Type.getType(String.class), new Type[]{Type.getType(String.class)}), null);
            Class<?> interfaceClass = interfaceMaker.create();

            // 代理对象
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(new Class[] {IGenericReference.class, interfaceClass});
            enhancer.setCallback(genericReferenceProxy);

            return (IGenericReference) enhancer.create();
        });
    }
}
