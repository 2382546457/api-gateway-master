package com.xiaohe.gateway.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.mapper.ApplicationInterfaceMethodMapper;
import com.xiaohe.gateway.center.model.entity.ApplicationInterfaceMethod;
import com.xiaohe.gateway.center.service.ApplicationInterfaceMethodService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApplicationInterfaceMethodServiceImpl extends ServiceImpl<ApplicationInterfaceMethodMapper, ApplicationInterfaceMethod> implements ApplicationInterfaceMethodService {

    @Resource
    private ApplicationInterfaceMethodMapper applicationInterfaceMethodMapper;

    @Override
    public OperationResult<ApplicationInterfaceMethod> queryApplicationInterfaceMethod(OperationRequest<ApplicationInterfaceMethod> request) {
        List<ApplicationInterfaceMethod> list = applicationInterfaceMethodMapper.queryApplicationInterfaceMethodListByPage(request);
        return new OperationResult<>(list.size(), list);
    }
}
