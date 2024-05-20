package com.xiaohe.gateway.center.controller;

import com.xiaohe.gateway.center.common.ResponseCode;
import com.xiaohe.gateway.center.common.Result;
import com.xiaohe.gateway.center.model.vo.GatewayServerVO;
import com.xiaohe.gateway.center.service.GatewayServerDetailService;
import com.xiaohe.gateway.center.service.GatewayServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/wg/admin/config")
public class ApiGatewayServerDetailController {

    private final Logger logger = LoggerFactory.getLogger(ApiGatewayServerDetailController.class);

    @Resource
    private GatewayServerDetailService gatewayServerDetailService;

    @Resource
    private GatewayServerService gatewayServerService;

    /**
     * 查询网关服务配置项信息
     * @return
     */
    @GetMapping(value = "/queryServerConfig", produces = "application/json;charset=utf-8")
    public Result<List<GatewayServerVO>> queryServerConfig() {
        try {
            List<GatewayServerVO> gatewayServerVOS = gatewayServerService.queryServerConfig();
            return new Result<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), gatewayServerVOS);
        } catch (Exception e) {
            return new Result<>(ResponseCode.UN_ERROR.getCode(), e.getMessage(), null);
        }
    }

    /**
     * 注册网关服务节点
     *
     * @param groupId        分组标识
     * @param gatewayId      网关标识
     * @param gatewayName    网关名称
     * @param gatewayAddress 网关地址
     * @return
     */
    @PostMapping("/registerGateway")
    public Result<Boolean> registerGatewayServerNode(@RequestParam String groupId,
                                                     @RequestParam String gatewayId,
                                                     @RequestParam String gatewayName,
                                                     @RequestParam String gatewayAddress) {
        try {
            boolean done = gatewayServerDetailService.registerGatewayServerNode(groupId, gatewayId, gatewayName, gatewayAddress);
            return new Result<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), done);
        } catch (Exception e) {
            logger.error("注册网关服务节点异常, groupId : {}, gatewayId : {}, gatewayName : {}, gatewayAddress : {}",
                    groupId,
                    gatewayId,
                    gatewayName,
                    gatewayAddress
            );
            return new Result<>(ResponseCode.UN_ERROR.getCode(), e.getMessage(), false);
        }
    }

    @PostMapping(value = "/distributionGateway")
    public void distributionGatewayServerNode(@RequestParam String groupId, @RequestParam String gatewayId) {

    }
}
