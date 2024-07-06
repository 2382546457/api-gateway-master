package com.xiaohe.gateway.center.controller;


import com.alibaba.fastjson.JSON;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.model.entity.*;
import com.xiaohe.gateway.center.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * 提供给前端ERP访问数据的controller
 */
@CrossOrigin
@RestController
@RequestMapping("/wg/admin/data")
public class ErpController {
    private static final Logger logger = LoggerFactory.getLogger(ErpController.class);

    @Resource
    private GatewayServerService gatewayServerService;

    @Resource
    private GatewayServerDetailService gatewayServerDetailService;

    @Resource
    private GatewayDistributionService gatewayDistributionService;

    @Resource
    private ApplicationSystemService applicationSystemService;

    @Resource
    private ApplicationInterfaceService applicationInterfaceService;

    @Resource
    private ApplicationInterfaceMethodService applicationInterfaceMethodService;

    /**
     * 查看网关分组
     * @param groupId
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/queryGatewayServer")
    public OperationResult<GatewayServer> queryGatewayServer(@RequestParam String groupId,
                                                              @RequestParam String page,
                                                              @RequestParam String limit) {
        OperationRequest<String> request = new OperationRequest<>(page, limit);
        request.setData(groupId);
        OperationResult<GatewayServer> result = gatewayServerService.queryGatewayServer(request);
        return result;
    }

    /**
     * 查看网关算力节点
     * @param groupId
     * @param gatewayId
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "queryGatewayServerDetail", produces = "application/json;charset=utf-8")
    public OperationResult<GatewayServerDetail> queryGatewayServerDetail(@RequestParam String groupId,
                                                                         @RequestParam String gatewayId,
                                                                         @RequestParam String page,
                                                                         @RequestParam String limit) {
        OperationRequest<GatewayServerDetail> req = new OperationRequest<>(page, limit);
        req.setData(new GatewayServerDetail().setGroupId(groupId).setGatewayId(gatewayId));
        return gatewayServerDetailService.queryGatewayServerDetail(req);
    }

    @GetMapping(value = "queryGatewayDistribution", produces = "application/json;charset=utf-8")
    public OperationResult<GatewayDistribution> queryGatewayDistribution(@RequestParam String groupId,
                                                                               @RequestParam String gatewayId,
                                                                               @RequestParam String page,
                                                                               @RequestParam String limit) {
        try {

            OperationRequest<GatewayDistribution> req = new OperationRequest<>(page, limit);
            req.setData(new GatewayDistribution().setGroupId(groupId).setGatewayId(gatewayId));
            OperationResult<GatewayDistribution> operationResult = gatewayDistributionService.queryGatewayDistribution(req);

            return operationResult;
        } catch (Exception e) {
            logger.error("查询网关分配数据异常 groupId：{}", groupId, e);
            return new OperationResult<>(0, null);
        }
    }

    @GetMapping(value = "queryApplicationSystem", produces = "application/json;charset=utf-8")
    public OperationResult<ApplicationSystem> queryApplicationSystem(@RequestParam String systemId,
                                                                     @RequestParam String systemName,
                                                                     @RequestParam String page,
                                                                     @RequestParam String limit) {
        try {
            OperationRequest<ApplicationSystem> req = new OperationRequest<>(page, limit);
            req.setData(new ApplicationSystem().setSystemId(systemId).setSystemName(systemName));
            OperationResult<ApplicationSystem> operationResult = applicationSystemService.queryApplicationSystem(req);
            return operationResult;
        } catch (Exception e) {
            logger.error("查询应用系统信息异常 systemId：{} systemName：{}", systemId, systemId, e);
            return new OperationResult<>(0, null);
        }
    }

    @GetMapping(value = "queryApplicationInterface", produces = "application/json;charset=utf-8")
    public OperationResult<ApplicationInterface> queryApplicationInterface(@RequestParam String systemId,
                                                                           @RequestParam String interfaceId,
                                                                           @RequestParam String page,
                                                                           @RequestParam String limit) {
        try {
            OperationRequest<ApplicationInterface> req = new OperationRequest<>(page, limit);
            req.setData(new ApplicationInterface().setSystemId(systemId).setInterfaceId(interfaceId));
            return applicationInterfaceService.queryApplicationInterface(req);
        } catch (Exception e) {
            logger.error("查询应用接口信息异常 systemId：{} interfaceId：{}", systemId, interfaceId, e);
            return new OperationResult<>(0, null);
        }
    }

    @GetMapping(value = "queryApplicationInterfaceMethodList", produces = "application/json;charset=utf-8")
    public OperationResult<ApplicationInterfaceMethod> queryApplicationInterfaceMethodList(@RequestParam String systemId,
                                                                                                 @RequestParam String interfaceId,
                                                                                                 @RequestParam String page,
                                                                                                 @RequestParam String limit) {
        try {
            OperationRequest<ApplicationInterfaceMethod> req = new OperationRequest<>(page, limit);
            req.setData(new ApplicationInterfaceMethod().setSystemId(systemId).setInterfaceId(interfaceId));
            OperationResult<ApplicationInterfaceMethod> operationResult = applicationInterfaceMethodService.queryApplicationInterfaceMethod(req);
            return operationResult;
        } catch (Exception e) {
            logger.error("查询应用接口方法信息异常 systemId：{} interfaceId：{}", systemId, interfaceId, e);
            return new OperationResult<>(0, null);
        }
    }

}
