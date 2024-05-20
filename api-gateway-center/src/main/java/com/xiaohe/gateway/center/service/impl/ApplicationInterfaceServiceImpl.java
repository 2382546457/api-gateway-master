package com.xiaohe.gateway.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.gateway.center.mapper.ApplicationInterfaceMapper;
import com.xiaohe.gateway.center.model.entity.ApplicationInterface;
import com.xiaohe.gateway.center.service.ApplicationInterfaceService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationInterfaceServiceImpl extends ServiceImpl<ApplicationInterfaceMapper, ApplicationInterface> implements ApplicationInterfaceService {
}
