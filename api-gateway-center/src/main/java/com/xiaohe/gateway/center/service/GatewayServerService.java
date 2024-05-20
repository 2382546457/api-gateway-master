package com.xiaohe.gateway.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohe.gateway.center.model.entity.GatewayServer;
import com.xiaohe.gateway.center.model.vo.GatewayServerVO;

import java.util.List;

public interface GatewayServerService extends IService<GatewayServer> {
    public List<GatewayServerVO> queryServerConfig();

}
