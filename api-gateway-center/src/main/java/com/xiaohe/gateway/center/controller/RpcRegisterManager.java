package com.xiaohe.gateway.center.controller;


import com.xiaohe.gateway.center.common.ResponseCode;
import com.xiaohe.gateway.center.common.Result;
import com.xiaohe.gateway.center.model.entity.ApplicationInterface;
import com.xiaohe.gateway.center.model.entity.ApplicationInterfaceMethod;
import com.xiaohe.gateway.center.model.entity.ApplicationSystem;
import com.xiaohe.gateway.center.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * RPC服务注册管理
 */
@RestController
@RequestMapping("/wg/admin/register")
public class RpcRegisterManager {

    private final Logger logger = LoggerFactory.getLogger(RpcRegisterManager.class);

    @Resource
    private ApplicationSystemService applicationSystemService;

    @Resource
    private ApplicationInterfaceService applicationInterfaceService;

    @Resource
    private ApplicationInterfaceMethodService applicationInterfaceMethodService;

    @Resource
    private GatewayDistributionService gatewayDistributionService;

    @Resource
    private MessageService messageService;

    /**
     * 注册应用服务
     * @param systemId
     * @param systemName
     * @param systemType
     * @param systemRegistry
     * @return
     */
    @PostMapping(value = "registerApplication", produces = "application/json;charset=utf-8")
    public Result<Boolean> registerApplication(@RequestParam String systemId,
                                               @RequestParam String systemName,
                                               @RequestParam String systemType,
                                               @RequestParam String systemRegistry) {
        try {
            ApplicationSystem applicationSystem = new ApplicationSystem();
            applicationSystem.setSystemName(systemName);
            applicationSystem.setSystemId(systemId);
            applicationSystem.setSystemRegistry(systemRegistry);
            applicationSystem.setSystemType(systemType);
            applicationSystem.setCreateTime(new Date());
            applicationSystem.setUpdateTime(new Date());
            applicationSystemService.save(applicationSystem);
            return new Result<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), true);
        } catch (DuplicateKeyException e) {
            return new Result<>(ResponseCode.INDEX_DUP.getCode(), e.getMessage(), true);
        } catch (Exception e) {
            return new Result<>(ResponseCode.UN_ERROR.getCode(), e.getMessage(), false);
        }
    }

    @PostMapping(value = "/registerApplicationInterface", produces = "application/json;charset=utf-8")
    public Result<Boolean> registerApplicationInterface(@RequestParam ApplicationInterface applicationInterface) {
        try {
            applicationInterfaceService.save(applicationInterface);
            return new Result<>(ResponseCode.SUCCESS.getCode(), "注册成功", true);
        } catch (DuplicateKeyException e) {
            return new Result<>(ResponseCode.INDEX_DUP.getCode(), e.getMessage(), true);
        } catch (Exception e) {
            return new Result<>(ResponseCode.UN_ERROR.getCode(), e.getMessage(), false);
        }
    }

    @PostMapping(value = "/registerApplicationInterfaceMethod", produces = "application/json;charset=utf-8")
    public Result<Boolean> registerApplicationInterfaceMethod(@RequestParam ApplicationInterfaceMethod applicationInterfaceMethod) {
        try {
            applicationInterfaceMethodService.save(applicationInterfaceMethod);
            return new Result<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), true);
        } catch (DuplicateKeyException e) {
            return new Result<>(ResponseCode.INDEX_DUP.getCode(), e.getMessage(), true);
        } catch (Exception e) {
            return new Result<>(ResponseCode.UN_ERROR.getCode(), e.getMessage(), false);
        }
    }

    /**
     * 网关服务注册
     * @param systemId
     * @return
     */
    @PostMapping(value = "registerEvent", produces = "application/json;charset=utf-8")
    public Result<Boolean> registerEvent(@RequestParam String systemId) {
        logger.info("应用注册: {}", systemId);
        String gatewayId = gatewayDistributionService.queryGatewayDistribution(systemId);
        messageService.pushMessage(gatewayId, systemId);
        return Result.buildSuccess(true);
    }


    /**
     * 查询redis配置
     * @return
     */
    @PostMapping(value = "queryRedisConfig", produces = "application/json;charset=utf-8")
    public Result<Map<String, String>> queryRedisConfig() {
        return Result.buildSuccess(messageService.queryRedisConfig());
    }
}
