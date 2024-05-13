package com.xiaohe.gateway.bind;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;

/**
 * 泛化调用静态代理，方便做一些拦截处理。
 * 给 http 对应的 RPC 调用做一层代理控制。每调用一个 http 对应的网管方法，就会以代理的方式调用 RPC 对应的泛化调用方法上
 */
public class GenericReferenceProxy implements MethodInterceptor {
    /**
     * RPC 泛化调用服务
     */
    private final GenericService genericService;

    /**
     * RPC泛化调用方法名称
     */
    private final String methodName;

    public GenericReferenceProxy(GenericService genericService, String methodName) {
        this.genericService = genericService;
        this.methodName = methodName;
    }


    /**
     * 做一层代理控制，后续不仅可以使用 Dubbo 泛化调用，也可以使用其他泛化调用
     * @param obj
     * @param method
     * @param args
     * @param proxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameters = new String[parameterTypes.length];

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = parameterTypes[i].getName();
        }
        // 举例: genericService.$invoke("sayHi", new String[]{"java.lang.String"}, new Object[]{"world"})
        return genericService.$invoke(methodName, parameters, args);
    }
}
