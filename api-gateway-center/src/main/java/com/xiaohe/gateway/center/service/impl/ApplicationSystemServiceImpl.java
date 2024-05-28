package com.xiaohe.gateway.center.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.gateway.center.mapper.ApplicationInterfaceMapper;
import com.xiaohe.gateway.center.mapper.ApplicationSystemMapper;
import com.xiaohe.gateway.center.model.entity.ApplicationSystem;
import com.xiaohe.gateway.center.model.entity.GatewayDistribution;
import com.xiaohe.gateway.center.model.vo.ApplicationInterfaceVO;
import com.xiaohe.gateway.center.model.vo.ApplicationSystemRichInfo;
import com.xiaohe.gateway.center.model.vo.ApplicationSystemVO;
import com.xiaohe.gateway.center.service.ApplicationSystemService;
import com.xiaohe.gateway.center.service.GatewayDistributionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationSystemServiceImpl extends ServiceImpl<ApplicationSystemMapper, ApplicationSystem> implements ApplicationSystemService {
    @Resource
    private GatewayDistributionService gatewayDistributionService;

    @Resource
    private ApplicationInterfaceMapper applicationInterfaceMapper;


    @Override
    public ApplicationSystemRichInfo queryAapplicationSystemRichInfo(String gatewayId, String systemId) {
        List<String> systemIdList = Collections.emptyList();
        // 如果指定了 systemId, 就查找指定，没指定就查全部
        if (StringUtils.hasText(systemId)) {
            systemIdList.add(systemId);
        } else {
            LambdaQueryWrapper<GatewayDistribution> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(GatewayDistribution::getGatewayId, gatewayId);
            systemIdList = gatewayDistributionService.list(lambdaQueryWrapper)
                    .stream().map(GatewayDistribution::getSystemId).collect(Collectors.toList());
        }
        // 查询系统id(systemID) 对应的系统列表信息, 一般来说会指定，所以只查出来一个 ApplicationSystem
        LambdaQueryWrapper<ApplicationSystem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(!CollectionUtils.isEmpty(systemIdList),ApplicationSystem::getSystemId, systemIdList);
        List<ApplicationSystem> applicationSystemList = baseMapper.selectList(lambdaQueryWrapper);
        List<ApplicationSystemVO> applicationSystemVOList = applicationSystemList.stream().map(applicationSystem -> {
            ApplicationSystemVO applicationSystemVO = new ApplicationSystemVO();
            BeanUtil.copyProperties(applicationSystem, applicationSystemVO);
            return applicationSystemVO;
        }).collect(Collectors.toList());

        // 查询系统下的接口信息
        for (ApplicationSystemVO applicationSystemVO : applicationSystemVOList) {
            String systemId1 = applicationSystemVO.getSystemId();
            List<ApplicationInterfaceVO> applicationInterfaceVOList = applicationInterfaceMapper.queryApplicationInterfaceVO(systemId1);
            applicationSystemVO.setInterfaceList(applicationInterfaceVOList);
        }
        return new ApplicationSystemRichInfo(gatewayId, applicationSystemVOList);
    }
}
