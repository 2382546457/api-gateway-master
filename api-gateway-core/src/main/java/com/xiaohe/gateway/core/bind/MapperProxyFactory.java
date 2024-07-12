package com.xiaohe.gateway.core.bind;

import com.xiaohe.gateway.core.mapping.HttpStatement;
import com.xiaohe.gateway.core.session.GatewaySession;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 生产 MapperMethod 的工厂
 */
public class MapperProxyFactory {
    private String uri;

    /**
     * key : uri
     * value : 泛化调用接口
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
            // IGenericReference 统一泛化调用接口
            // interfaceClass    根据泛化调用注册信息创建的接口，建立 http -> rpc 关联
            enhancer.setInterfaces(new Class[]{IGenericReference.class, interfaceClass});
            enhancer.setCallback(genericReferenceProxy);
            return (IGenericReference) enhancer.create();
        });
    }

}
