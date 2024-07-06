package com.xiaohe.gateway.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.model.entity.GatewayServer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GatewayServerMapper extends BaseMapper<GatewayServer> {
    public List<GatewayServer> queryGatewayServer(OperationRequest<String> request);
}
