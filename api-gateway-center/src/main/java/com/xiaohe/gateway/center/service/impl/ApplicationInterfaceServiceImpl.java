package com.xiaohe.gateway.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.mapper.ApplicationInterfaceMapper;
import com.xiaohe.gateway.center.model.entity.ApplicationInterface;
import com.xiaohe.gateway.center.service.ApplicationInterfaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApplicationInterfaceServiceImpl extends ServiceImpl<ApplicationInterfaceMapper, ApplicationInterface> implements ApplicationInterfaceService {
    @Resource
    private ApplicationInterfaceMapper applicationInterfaceMapper;

    @Override
    public OperationResult<ApplicationInterface> queryApplicationInterface(OperationRequest<ApplicationInterface> request) {
        List<ApplicationInterface> list = applicationInterfaceMapper.queryApplicationInterfaceListByPage(request);
        return new OperationResult<>(list.size(), list);
    }
}
