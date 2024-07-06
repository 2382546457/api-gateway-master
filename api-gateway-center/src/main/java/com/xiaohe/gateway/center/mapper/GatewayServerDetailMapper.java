package com.xiaohe.gateway.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.model.entity.GatewayServerDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GatewayServerDetailMapper extends BaseMapper<GatewayServerDetail> {
    public List<GatewayServerDetail> queryGatewayServerDetail(OperationRequest<GatewayServerDetail> request);
}
