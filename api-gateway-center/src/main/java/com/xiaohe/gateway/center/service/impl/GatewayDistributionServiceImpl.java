package com.xiaohe.gateway.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.gateway.center.mapper.GatewayDistributionMapper;
import com.xiaohe.gateway.center.model.entity.GatewayDistribution;
import com.xiaohe.gateway.center.service.GatewayDistributionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GatewayDistributionServiceImpl extends ServiceImpl<GatewayDistributionMapper, GatewayDistribution> implements GatewayDistributionService {
    @Resource
    private GatewayDistributionMapper gatewayDistributionMapper;

    @Override
    public String queryGatewayDistribution(String systemId) {
        return gatewayDistributionMapper.queryGatewayDistribution(systemId);
    }
}
