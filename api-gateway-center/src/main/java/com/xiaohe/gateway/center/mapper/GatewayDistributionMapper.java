package com.xiaohe.gateway.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.gateway.center.model.entity.GatewayDistribution;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GatewayDistributionMapper extends BaseMapper<GatewayDistribution> {

    String queryGatewayDistribution(String systemId);

}
