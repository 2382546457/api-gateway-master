package com.xiaohe.gateway.assist.config;


import com.xiaohe.gateway.assist.application.GatewayApplication;
import com.xiaohe.gateway.assist.service.GatewayCenterService;
import com.xiaohe.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import com.xiaohe.gateway.core.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Configuration
@EnableConfigurationProperties(GatewayServiceProperties.class)
public class GatewayAutoConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayAutoConfig.class);

    @Bean
    public GatewayCenterService registerGatewayService() {
        return new GatewayCenterService();
    }




    @Bean
    public GatewayApplication gatewayApplication(GatewayServiceProperties properties,
                                                 GatewayCenterService registerGatewayService,
                                                 com.xiaohe.gateway.core.session.Configuration configuration) {
        return new GatewayApplication(properties, registerGatewayService, configuration);
    }

    /**
     * 注册网关配置对象
     * @param properties
     * @return
     */
    @Bean
    public com.xiaohe.gateway.core.session.Configuration gatewayCoreConfiguration(GatewayServiceProperties properties) {
        com.xiaohe.gateway.core.session.Configuration configuration = new com.xiaohe.gateway.core.session.Configuration();
        String[] split = properties.getGatewayAddress().split(":");
        configuration.setHostName(split[0].trim());
        configuration.setPort(Integer.parseInt(split[1]));
        return configuration;
    }

    /**
     * 初始化网关服务，创建服务端 Channel 对象，方便获取和控制网关操作
     * @param configuration
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Bean
    public Channel initGateway(com.xiaohe.gateway.core.session.Configuration configuration) throws ExecutionException, InterruptedException {
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);
        GatewaySocketServer server = new GatewaySocketServer(configuration, gatewaySessionFactory);
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel) throw new RuntimeException("api gateway core netty server start error channel is null");
        while (!channel.isActive()) {
            Thread.sleep(500);
        }
        logger.info("api-gateway-core netty server start! {}", channel.localAddress());
        return channel;
    }


}
