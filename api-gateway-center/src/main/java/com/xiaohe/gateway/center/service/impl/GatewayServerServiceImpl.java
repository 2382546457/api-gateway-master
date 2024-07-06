package com.xiaohe.gateway.center.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.mapper.GatewayServerMapper;
import com.xiaohe.gateway.center.model.entity.GatewayServer;
import com.xiaohe.gateway.center.model.vo.GatewayServerVO;
import com.xiaohe.gateway.center.service.GatewayServerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GatewayServerServiceImpl extends ServiceImpl<GatewayServerMapper, GatewayServer> implements GatewayServerService {

    @Resource
    private GatewayServerMapper gatewayServerMapper;

    @Override
    public List<GatewayServerVO> queryServerConfig() {
        LambdaQueryWrapper<GatewayServer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(GatewayServer::getId);
        lambdaQueryWrapper.select(GatewayServer::getGroupId);
        lambdaQueryWrapper.select(GatewayServer::getGroupName);
        List<GatewayServer> gatewayServers = baseMapper.selectList(lambdaQueryWrapper);
        List<GatewayServerVO> gatewayServerVOS = gatewayServers.stream().map(gatewayServer -> {
            GatewayServerVO gatewayServerVO = new GatewayServerVO();
            BeanUtil.copyProperties(gatewayServer, gatewayServerVO);
            return gatewayServerVO;
        }).collect(Collectors.toList());
        return gatewayServerVOS;
    }

    @Override
    public OperationResult<GatewayServer> queryGatewayServer(OperationRequest<String> request) {
        List<GatewayServer> gatewayServers = gatewayServerMapper.queryGatewayServer(request);
        return new OperationResult<>(gatewayServers.size(), gatewayServers);
    }
}
