package com.xiaohe.gateway.assist.application;

import com.xiaohe.gateway.assist.config.GatewayServiceProperties;
import com.xiaohe.gateway.assist.service.RegisterGatewayService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


/**
 * 在 spring 准备好之后执行 onApplicationEvent 事件, 在这个事件中，将网关注册到网关中心
 */
public class GatewayApplication implements ApplicationListener<ContextRefreshedEvent> {

    private GatewayServiceProperties properties;

    private RegisterGatewayService registerGatewayService;

    public GatewayApplication(GatewayServiceProperties properties, RegisterGatewayService registerGatewayService) {
        this.properties = properties;
        this.registerGatewayService = registerGatewayService;
    }

    /**
     * 在使用网关的应用的 spring 启动之后，执行这个方法，将网关注册到网关中心
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        registerGatewayService.doRegister(
                properties.getAddress(),
                properties.getGroupId(),
                properties.getGatewayId(),
                properties.getGatewayName(),
                properties.getGatewayAddress()
        );
    }
}
