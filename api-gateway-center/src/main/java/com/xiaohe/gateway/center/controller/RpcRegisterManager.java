package com.xiaohe.gateway.center.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaohe.gateway.center.common.ResponseCode;
import com.xiaohe.gateway.center.common.Result;
import com.xiaohe.gateway.center.model.entity.*;
import com.xiaohe.gateway.center.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

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
    private GatewayServerDetailService gatewayServerDetailService;

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
            // 要求systemId唯一, 如果已存在就更新
            LambdaQueryWrapper<ApplicationSystem> applicationSystemLambdaQueryWrapper = new LambdaQueryWrapper<>();
            applicationSystemLambdaQueryWrapper.eq(ApplicationSystem::getSystemId, systemId);
            ApplicationSystem applicationSystem = applicationSystemService.getOne(applicationSystemLambdaQueryWrapper);
            if (applicationSystem == null) {
                applicationSystem = new ApplicationSystem();
            }
            applicationSystem.setSystemName(systemName);
            applicationSystem.setSystemId(systemId);
            applicationSystem.setSystemRegistry(systemRegistry);
            applicationSystem.setSystemType(systemType);
            applicationSystem.setCreateTime(new Date());
            applicationSystem.setUpdateTime(new Date());
            applicationSystemService.saveOrUpdate(applicationSystem);
            return new Result<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), true);
        } catch (DuplicateKeyException e) {
            return new Result<>(ResponseCode.INDEX_DUP.getCode(), e.getMessage(), true);
        } catch (Exception e) {
            return new Result<>(ResponseCode.UN_ERROR.getCode(), e.getMessage(), false);
        }
    }

    @PostMapping(value = "/registerApplicationInterface", produces = "application/json;charset=utf-8")
    public Result<Boolean> registerApplicationInterface(@RequestParam String systemId,
                                                        @RequestParam String interfaceId,
                                                        @RequestParam String interfaceName,
                                                        @RequestParam String interfaceVersion) {

        try {
            LambdaQueryWrapper<ApplicationInterface> applicationInterfaceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            applicationInterfaceLambdaQueryWrapper.eq(ApplicationInterface::getInterfaceId, interfaceId);
            ApplicationInterface applicationInterface = applicationInterfaceService.getOne(applicationInterfaceLambdaQueryWrapper);
            if (applicationInterface == null) {
                applicationInterface = new ApplicationInterface();
                applicationInterface.setCreateTime(new Date());
            }
            applicationInterface.setInterfaceId(interfaceId);
            applicationInterface.setInterfaceName(interfaceName);
            applicationInterface.setInterfaceVersion(interfaceVersion);
            applicationInterface.setSystemId(systemId);
            applicationInterfaceService.saveOrUpdate(applicationInterface);
            return new Result<>(ResponseCode.SUCCESS.getCode(), "注册成功", true);
        } catch (DuplicateKeyException e) {
            return new Result<>(ResponseCode.INDEX_DUP.getCode(), e.getMessage(), true);
        } catch (Exception e) {
            return new Result<>(ResponseCode.UN_ERROR.getCode(), e.getMessage(), false);
        }
    }

    @PostMapping(value = "/registerApplicationInterfaceMethod", produces = "application/json;charset=utf-8")
    public Result<Boolean> registerApplicationInterfaceMethod(@RequestParam String systemId,
                                                              @RequestParam String interfaceId,
                                                              @RequestParam String methodId,
                                                              @RequestParam String methodName,
                                                              @RequestParam String parameterType,
                                                              @RequestParam String uri,
                                                              @RequestParam String httpCommandType,
                                                              @RequestParam String auth) {
        try {
            LambdaQueryWrapper<ApplicationInterfaceMethod> applicationInterfaceMethodLambdaQueryWrapper = new LambdaQueryWrapper<>();
            applicationInterfaceMethodLambdaQueryWrapper.eq(ApplicationInterfaceMethod::getMethodId, methodId);
            ApplicationInterfaceMethod applicationInterfaceMethod = applicationInterfaceMethodService.getOne(applicationInterfaceMethodLambdaQueryWrapper);
            if (applicationInterfaceMethod == null) {
                applicationInterfaceMethod = new ApplicationInterfaceMethod();
                applicationInterfaceMethod.setCreateTime(new Date());
            }
            applicationInterfaceMethod.setInterfaceId(interfaceId);
            applicationInterfaceMethod.setSystemId(systemId);
            applicationInterfaceMethod.setMethodName(methodName);
            applicationInterfaceMethod.setParameterType(parameterType);
            applicationInterfaceMethod.setUri(uri);
            applicationInterfaceMethod.setHttpCommandType(httpCommandType);
            applicationInterfaceMethod.setAuth(Integer.valueOf(auth));
            applicationInterfaceMethodService.saveOrUpdate(applicationInterfaceMethod);
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
    public Result<Boolean> registerEvent(@RequestParam String systemId,
                                         @RequestParam String gatewayId,
                                         @RequestParam String systemName,
                                         @RequestParam String systemRegistry) {
        logger.info("应用注册: {}", systemId);
        GatewayDistribution gatewayDistribution = gatewayDistributionService.queryGatewayDistribution(systemId);
        // 如果是第一次注册，将 网关-业务项目 对应关系写入数据库
        if (Objects.isNull(gatewayDistribution)) {
            gatewayDistribution = new GatewayDistribution();
        }
        gatewayDistribution.setSystemId(systemId);
        gatewayDistribution.setSystemName(systemName);
        gatewayDistribution.setGatewayId(gatewayId);
        gatewayDistributionService.saveOrUpdate(gatewayDistribution);
        // 如果不是第一次注册
        LambdaQueryWrapper<GatewayServerDetail> gatewayServerDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        gatewayServerDetailLambdaQueryWrapper.eq(GatewayServerDetail::getGatewayId, gatewayId);
        GatewayServerDetail one = gatewayServerDetailService.getOne(gatewayServerDetailLambdaQueryWrapper);
        gatewayDistribution.setGroupId(one.getGroupId());

        // 发布业务项目注册的信息
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
