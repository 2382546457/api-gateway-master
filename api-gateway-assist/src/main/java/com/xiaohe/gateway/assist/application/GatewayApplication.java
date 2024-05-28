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
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;


/**
 * 在 spring 准备好之后执行 onApplicationEvent 事件, 在这个事件中，将网关注册到网关中心
 */
public class GatewayApplication implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

    private GatewayServiceProperties properties;

    private GatewayCenterService gatewayCenterService;

    private Configuration configuration;

    public GatewayApplication(GatewayServiceProperties properties,
                              GatewayCenterService gatewayCenterService,
                              Configuration configuration) {
        this.properties = properties;
        this.gatewayCenterService = gatewayCenterService;
        this.configuration = configuration;
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

        // 2. 拉取网关配置，每个网关都会在注册中心分配上需要映射的RPC服务信息，包括: 系统、接口、方法
        ApplicationSystemRichInfo applicationSystemRichInfo = gatewayCenterService.pullApplicationSystemRichInfo(properties.getAddress(), properties.getGatewayId());
        logger.info("从网关中心拉取信息 applicationSystemRichInfo:\n {}", JSON.toJSONString(applicationSystemRichInfo));
        List<ApplicationSystemVO> applicationSystemVOList = applicationSystemRichInfo.getApplicationSystemVOList();
        for (ApplicationSystemVO system : applicationSystemVOList) {
            List<ApplicationInterfaceVO> interfaceList = system.getInterfaceList();
            for (ApplicationInterfaceVO itf : interfaceList) {
                // 注册接口的配置信息
                configuration.registryConfig(system.getSystemId(), system.getSystemRegistry(), itf.getInterfaceId(), itf.getInterfaceVersion());
                // 注册接口中方法的配置信息
                List<ApplicationInterfaceMethodVO> methodList = itf.getMethodList();
                for (ApplicationInterfaceMethodVO method : methodList) {
                    HttpStatement httpStatement = new HttpStatement(
                            system.getSystemId(),
                            itf.getInterfaceId(),
                            method.getMethodName(),
                            method.getParameterType(),
                            method.getUri(),
                            HttpCommandType.valueOf(method.getHttpCommandType()),
                            method.isAuth()
                    );
                    configuration.addMapper(httpStatement);
                    logger.info("网关服务注册方法:   系统: {}, 接口: {}, 方法: {}.", system.getSystemId(), itf.getInterfaceId(), method.getMethodId());
                }
            }
        }
    }
}
