package com.xiaohe.gateway.bind;

import com.xiaohe.gateway.session.Configuration;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;

/**
 * 将 HTTP 的请求方法与对应的 RPC 方法做关系绑定
 */
public class GenericReferenceRegistry {
    private final Configuration configuration;

    public GenericReferenceRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 泛化调用静态代理工厂
     */
    private final Map<String, GenericReferenceProxyFactory> knownGenericReferences = new HashMap<>();

    /**
     * 根据 method 获取 泛化调用接口
     * @param methodName
     * @return
     */
    public IGenericReference getGenericReference(String methodName) {
        GenericReferenceProxyFactory genericReferenceProxyFactory = knownGenericReferences.get(methodName);
        if (genericReferenceProxyFactory == null) {
            throw new RuntimeException("Type " + methodName + " is not known to the GenericReferenceRegistry.");
        }
        return genericReferenceProxyFactory.newInstance(methodName);
    }

    /**
     * 注册泛化调用服务接口
     * @param application 服务，如 api-gateway-test
     * @param interfaceName 接口，如 com.xiaohe.gateway.rpc.IActivityBooth
     * @param methodName 方法名，如 sayHi
     */
    public void addGenericReference(String application, String interfaceName, String methodName) {
        // 获取基础服务，已经放在Map中缓存起来了
        ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
        RegistryConfig registryConfig = configuration.getRegistryConfig(application);
        ReferenceConfig<GenericService> reference = configuration.getReferenceConfig(interfaceName);

        // 构建 Dubbo 服务
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(applicationConfig).registry(registryConfig).reference(reference).start();
        // 获取泛化调用服务
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference);
        // 创建并保存泛化工厂
        knownGenericReferences.put(methodName, new GenericReferenceProxyFactory(genericService));


    }
}
