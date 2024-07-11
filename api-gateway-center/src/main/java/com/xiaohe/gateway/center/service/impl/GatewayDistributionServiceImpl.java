package com.xiaohe.gateway.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.mapper.GatewayDistributionMapper;
import com.xiaohe.gateway.center.model.entity.GatewayDistribution;
import com.xiaohe.gateway.center.service.GatewayDistributionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GatewayDistributionServiceImpl extends ServiceImpl<GatewayDistributionMapper, GatewayDistribution> implements GatewayDistributionService {
    @Resource
    private GatewayDistributionMapper gatewayDistributionMapper;

    @Override
    public GatewayDistribution queryGatewayDistribution(String systemId) {
        return gatewayDistributionMapper.queryGatewayDistribution(systemId);
    }

    @Override
    public OperationResult<GatewayDistribution> queryGatewayDistribution(OperationRequest<GatewayDistribution> request) {
        List<GatewayDistribution> list = gatewayDistributionMapper.queryGatewayDistributionListByPage(request);
        return new OperationResult<>(list.size(), list);
    }
}
