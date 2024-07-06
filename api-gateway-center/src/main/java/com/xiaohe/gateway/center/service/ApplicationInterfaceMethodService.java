package com.xiaohe.gateway.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.model.entity.ApplicationInterfaceMethod;

public interface ApplicationInterfaceMethodService extends IService<ApplicationInterfaceMethod> {
    OperationResult<ApplicationInterfaceMethod> queryApplicationInterfaceMethod(OperationRequest<ApplicationInterfaceMethod> request);

}
