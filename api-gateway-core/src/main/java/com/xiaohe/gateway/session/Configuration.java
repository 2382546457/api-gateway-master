package com.xiaohe.gateway.session;

import com.xiaohe.gateway.authorization.IAuth;
import com.xiaohe.gateway.authorization.auth.AuthService;
import com.xiaohe.gateway.bind.IGenericReference;
import com.xiaohe.gateway.bind.MapperRegistry;
import com.xiaohe.gateway.datasource.Connection;
import com.xiaohe.gateway.executor.Executor;
import com.xiaohe.gateway.executor.SimpleExecutor;
import com.xiaohe.gateway.mapping.HttpStatement;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private final MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 鉴权服务
     */
    private final IAuth auth = new AuthService();

    /**
     * 已经注册的服务
     * key : uri
     * value : httpStatement
     * 这个uri是httpStatement里面的uri，所以这个Map是用来 快速索引 的
     */
    private final Map<String, HttpStatement> httpStatements = new HashMap<>();

    /**
     * RPC应用配置项
     * key : 应用名
     * value : 应用配置
      */
    private final Map<String, ApplicationConfig> applicationConfigMap = new HashMap<>();
    /**
     * RPC注册中心配置项
     * key : 服务名
     * value : 该服务的注册信息
     */
    private final Map<String, RegistryConfig> registryConfigMap = new HashMap<>();
    /**
     * RPC泛化服务配置项
     * key : 接口名
     * value : 对应的泛化服务
     */
    private final Map<String, ReferenceConfig<GenericService>> referenceConfigMap = new HashMap<>();

    /**
     * 注册配置
     * @param applicationName
     * @param address
     * @param interfaceName
     * @param version
     */
    public synchronized void registryConfig(String applicationName, String address, String interfaceName, String version) {
        if (applicationConfigMap.get(applicationName) == null) {
            ApplicationConfig applicationConfig = new ApplicationConfig();
            applicationConfig.setName(applicationName);
            applicationConfig.setQosEnable(false);
            applicationConfigMap.put(applicationName, applicationConfig);
        }
        if (registryConfigMap.get(applicationName) == null) {
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setAddress(address);
            registryConfig.setRegister(false);
            registryConfigMap.put(applicationName, registryConfig);
        }
        if (referenceConfigMap.get(interfaceName) == null) {
            ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
            reference.setInterface(interfaceName);
            reference.setVersion(version);
            reference.setGeneric(true);
            referenceConfigMap.put(interfaceName, reference);
        }
    }

    /**
     * 向 Gateway 中注册一个服务
     * @param httpStatement
     */
    public void addMapper(HttpStatement httpStatement) {
        mapperRegistry.addMapper(httpStatement);
    }

    /**
     * mapperRegistry 中是 uri - MapperProxyFactory 的映射
     * getMapper 的逻辑是: 找到 MapperProxyFactory 后，
     * 调用 MapperProxyFactory.newInstance(gatewaySession) 生成一个 IGenericReference
     * @param uri
     * @param gatewaySession
     * @return
     */
    public IGenericReference getMapper(String uri, GatewaySession gatewaySession) {
        return mapperRegistry.getMapper(uri, gatewaySession);
    }

    public ApplicationConfig getApplicationConfig(String applicationName) {
        return this.applicationConfigMap.get(applicationName);
    }
    public RegistryConfig getRegistryConfig(String applicationName) {
        return this.registryConfigMap.get(applicationName);
    }
    public ReferenceConfig<GenericService> getReferenceConfig(String interfaceName) {
        return this.referenceConfigMap.get(interfaceName);
    }

    public void addHttpStatement(HttpStatement httpStatement) {
        this.httpStatements.put(httpStatement.getUri(), httpStatement);
    }
    public HttpStatement getHttpStatement(String uri) {
        return this.httpStatements.get(uri);
    }
    public Executor newExecutor(Connection connection) {
        return new SimpleExecutor(this, connection);
    }


    public boolean authValidate(String uId, String token) {
        return auth.validate(uId, token);
    }

}
