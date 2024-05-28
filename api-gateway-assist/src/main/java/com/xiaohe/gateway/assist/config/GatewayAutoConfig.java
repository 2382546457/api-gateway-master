package com.xiaohe.gateway.assist.config;


import com.xiaohe.gateway.assist.application.GatewayApplication;
import com.xiaohe.gateway.assist.service.GatewayCenterService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GatewayServiceProperties.class)
public class GatewayAutoConfig {

    @Bean
    public GatewayCenterService registerGatewayService() {
        return new GatewayCenterService();
    }

    @Bean
    public GatewayApplication gatewayApplication(GatewayServiceProperties properties, GatewayCenterService registerGatewayService) {
        return new GatewayApplication(properties, registerGatewayService);
    }
}
