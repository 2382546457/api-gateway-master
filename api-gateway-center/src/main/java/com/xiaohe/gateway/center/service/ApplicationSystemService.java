package com.xiaohe.gateway.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.model.entity.ApplicationSystem;
import com.xiaohe.gateway.center.model.vo.ApplicationSystemRichInfo;

public interface ApplicationSystemService extends IService<ApplicationSystem> {
    public ApplicationSystemRichInfo queryAapplicationSystemRichInfo(String gatewayId, String systemId);

    OperationResult<ApplicationSystem> queryApplicationSystem(OperationRequest<ApplicationSystem> request);

}
