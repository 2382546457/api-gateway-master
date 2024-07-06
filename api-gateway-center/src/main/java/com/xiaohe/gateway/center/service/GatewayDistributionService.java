package com.xiaohe.gateway.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.model.entity.GatewayDistribution;

public interface GatewayDistributionService extends IService<GatewayDistribution> {
    String queryGatewayDistribution(String systemId);

    OperationResult<GatewayDistribution> queryGatewayDistribution(OperationRequest<GatewayDistribution> request);
}
