package com.xiaohe.gateway.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.model.entity.ApplicationInterface;

public interface ApplicationInterfaceService extends IService<ApplicationInterface> {
    OperationResult<ApplicationInterface> queryApplicationInterface(OperationRequest<ApplicationInterface> request);

}
