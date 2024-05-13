package com.xiaohe.gateway.bind;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.dubbo.rpc.service.GenericService;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 泛化调用静态代理工厂
 */
public class GenericReferenceProxyFactory {
    /**
     * 泛化调用服务
     */
    private final GenericService genericService;

    private final Map<String, IGenericReference> genericReferenceCache = new ConcurrentHashMap<>();

    public GenericReferenceProxyFactory(GenericService genericService) {
        this.genericService = genericService;
    }


    /**
     * 创建一个泛化调用
     * @param method
     * @return
     */
    public IGenericReference newInstance(String method) {
        return genericReferenceCache.computeIfAbsent(method, k -> {
            // 创建泛化调用代理
            GenericReferenceProxy genericReferenceProxy = new GenericReferenceProxy(genericService, method);
            // 创建接口
            InterfaceMaker interfaceMaker = new InterfaceMaker();
            interfaceMaker.add(new Signature(method, Type.getType(String.class), new Type[]{Type.getType(String.class)}), null);
            Class<?> interfaceClass = interfaceMaker.create();

            // 代理对象
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Objects.class);
            // IGenericReference 统一泛化调用接口
            // interfaceClass    根据泛化调用注册信息创建的接口，建立 http -> rpc 关联
            enhancer.setInterfaces(new Class[]{IGenericReference.class, interfaceClass});
            enhancer.setCallback(genericReferenceProxy);

            return (IGenericReference) enhancer.create();

        });
    }

}
