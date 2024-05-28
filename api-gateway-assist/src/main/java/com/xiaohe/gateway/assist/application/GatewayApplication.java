package com.xiaohe.gateway.assist.application;

import com.alibaba.fastjson.JSON;
import com.xiaohe.gateway.assist.config.GatewayServiceProperties;
import com.xiaohe.gateway.assist.model.aggregates.ApplicationSystemRichInfo;
import com.xiaohe.gateway.assist.service.GatewayCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


/**
 * 在 spring 准备好之后执行 onApplicationEvent 事件, 在这个事件中，将网关注册到网关中心
 */
public class GatewayApplication implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

    private GatewayServiceProperties properties;

    private GatewayCenterService gatewayCenterService;

    public GatewayApplication(GatewayServiceProperties properties, GatewayCenterService gatewayCenterService) {
        this.properties = properties;
        this.gatewayCenterService = gatewayCenterService;
    }

    /**
     * 在使用网关的应用的 spring 启动之后，执行这个方法，将网关注册到网关中心
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 1. 注册
        gatewayCenterService.doRegister(
                properties.getAddress(),
                properties.getGroupId(),
                properties.getGatewayId(),
                properties.getGatewayName(),
                properties.getGatewayAddress()
        );

        // 2. 拉取
        ApplicationSystemRichInfo applicationSystemRichInfo = gatewayCenterService.pullApplicationSystemRichInfo(properties.getAddress(), properties.getGatewayId());
        logger.info("从网关中心拉取信息 applicationSystemRichInfo:\n {}", JSON.toJSONString(applicationSystemRichInfo));
    }
}
