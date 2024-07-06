package com.xiaohe.gateway.assist.application;

import com.alibaba.fastjson.JSON;
import com.xiaohe.gateway.assist.config.GatewayServiceProperties;
import com.xiaohe.gateway.assist.model.aggregates.ApplicationSystemRichInfo;
import com.xiaohe.gateway.assist.model.vo.ApplicationInterfaceMethodVO;
import com.xiaohe.gateway.assist.model.vo.ApplicationInterfaceVO;
import com.xiaohe.gateway.assist.model.vo.ApplicationSystemVO;
import com.xiaohe.gateway.assist.service.GatewayCenterService;
import com.xiaohe.gateway.core.mapping.HttpCommandType;
import com.xiaohe.gateway.core.mapping.HttpStatement;
import com.xiaohe.gateway.core.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;


import io.netty.channel.Channel;
import java.util.List;


/**
 * 在 spring 准备好之后执行 onApplicationEvent 事件, 在这个事件中，将网关注册到网关中心
 */
public class GatewayApplication implements ApplicationListener<ContextClosedEvent>, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

    private GatewayServiceProperties properties;

    private GatewayCenterService gatewayCenterService;

    private Configuration configuration;

    private Channel gatewaySocketChanel;

    public GatewayApplication(GatewayServiceProperties properties,
                              GatewayCenterService gatewayCenterService,
                              Configuration configuration,
                              Channel gatewaySocketChanel) {
        this.properties = properties;
        this.gatewayCenterService = gatewayCenterService;
        this.configuration = configuration;
        this.gatewaySocketChanel = gatewaySocketChanel;
    }


    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        try {
            if (gatewaySocketChanel.isActive()) {
                logger.info("应用容器关闭，API网关服务关闭。localAddress: {}.", gatewaySocketChanel.localAddress());
                gatewaySocketChanel.close();
            }
        } catch (Exception e) {
            logger.error("应用容器关闭，API网关服务关闭失败");
        }
    }

    /**
     * 在使用网关的应用的 spring 启动之后，执行这个方法，将网关注册到网关中心， 并且拉取其他所有的 HTTP-RPC 映射
     *
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 1. 注册
        gatewayCenterService.doRegister(
                properties.getAddress(),
                properties.getGroupId(),
                properties.getGatewayId(),
                properties.getGatewayName(),
                properties.getGatewayAddress()
        );
        // 启动时拉取所有 systemId
        addMappers("");
    }

    public void receiveMessage(Object message) {
        logger.info("【事件通知】接收注册中心推送消息 message：{}", message);
        addMappers(message.toString().substring(1, message.toString().length() - 1));
    }

    public void addMappers(String systemId) {
        // 2. 拉取网关配置；每个网关算力都会在注册中心分配上需要映射的RPC服务信息，包括；系统、接口、方法
        ApplicationSystemRichInfo applicationSystemRichInfo = gatewayCenterService.pullApplicationSystemRichInfo(properties.getAddress(), properties.getGatewayId(), systemId);
        List<ApplicationSystemVO> applicationSystemVOList = applicationSystemRichInfo.getApplicationSystemVOList();
        if (applicationSystemVOList.isEmpty()) {
            logger.warn("网关{}服务注册映射为空，请排查 gatewayCenterService.pullApplicationSystemRichInfo 是否检索到此网关算力需要拉取的配置数据。", systemId);
            return;
        }
        for (ApplicationSystemVO system : applicationSystemVOList) {
            List<ApplicationInterfaceVO> interfaceList = system.getInterfaceList();
            for (ApplicationInterfaceVO itf : interfaceList) {
                // 2.1 创建配置信息加载注册
                configuration.registryConfig(system.getSystemId(), system.getSystemRegistry(), itf.getInterfaceId(), itf.getInterfaceVersion());
                List<ApplicationInterfaceMethodVO> methodList = itf.getMethodList();
                // 2.2 注册系统服务接口信息
                for (ApplicationInterfaceMethodVO method : methodList) {
                    HttpStatement httpStatement = new HttpStatement(
                            system.getSystemId(),
                            itf.getInterfaceId(),
                            method.getMethodId(),
                            method.getParameterType(),
                            method.getUri(),
                            HttpCommandType.valueOf(method.getHttpCommandType()),
                            method.isAuth());
                    configuration.addMapper(httpStatement);
                    logger.info("网关服务注册映射 系统：{} 接口：{} 方法：{}", system.getSystemId(), itf.getInterfaceId(), method.getMethodId());
                }
            }
        }
    }
}
