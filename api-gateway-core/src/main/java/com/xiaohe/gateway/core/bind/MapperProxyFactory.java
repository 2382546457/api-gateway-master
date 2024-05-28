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
        IGenericReference reference = this.genericReferenceCache.get(uri);
        if (reference != null) {
            return reference;
        }
        HttpStatement httpStatement = gatewaySession.getConfiguration().getHttpStatement(uri);
        // 获取代理
        MapperProxy genericReferenceProxy = new MapperProxy(gatewaySession, uri);
        // 创建接口
        InterfaceMaker interfaceMaker = new InterfaceMaker();
        interfaceMaker.add(new Signature(httpStatement.getMethodName(), Type.getType(String.class), new Type[]{Type.getType(String.class)}), null);
        Class<?> interfaceClass = interfaceMaker.create();
        // 创建代理, 让代理对象 implements IGenericReference、interfaceMaker.create()，那么就可以将创建的代理对象转换为 IGenericReference 统一管理
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Object.class);
        enhancer.setInterfaces(new Class[]{ IGenericReference.class, interfaceClass});
        enhancer.setCallback(genericReferenceProxy);
        return (IGenericReference) enhancer.create();
    }

}
