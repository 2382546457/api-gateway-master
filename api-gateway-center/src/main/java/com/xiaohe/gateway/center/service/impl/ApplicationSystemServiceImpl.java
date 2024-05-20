package com.xiaohe.gateway.center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.gateway.center.mapper.ApplicationSystemMapper;
import com.xiaohe.gateway.center.model.entity.ApplicationSystem;
import com.xiaohe.gateway.center.service.ApplicationSystemService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationSystemServiceImpl extends ServiceImpl<ApplicationSystemMapper, ApplicationSystem> implements ApplicationSystemService {
}
