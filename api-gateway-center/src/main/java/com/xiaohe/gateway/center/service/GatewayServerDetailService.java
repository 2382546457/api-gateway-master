package com.xiaohe.gateway.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.model.entity.GatewayServerDetail;

public interface GatewayServerDetailService extends IService<GatewayServerDetail> {
    /**
     * 注册网关服务
     * @param groupId
     * @param gatewayId
     * @param gatewayName
     * @param gatewayAddress
     * @return
     */
    public boolean registerGatewayServerNode(String groupId, String gatewayId, String gatewayName, String gatewayAddress);

    public OperationResult<GatewayServerDetail> queryGatewayServerDetail(OperationRequest<GatewayServerDetail> request);
}
