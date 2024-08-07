package com.xiaohe.gateway.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.gateway.center.common.Constants;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.common.OperationResult;
import com.xiaohe.gateway.center.mapper.GatewayServerDetailMapper;
import com.xiaohe.gateway.center.model.entity.GatewayServerDetail;
import com.xiaohe.gateway.center.service.GatewayServerDetailService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class GatewayServerDetailServiceImpl extends ServiceImpl<GatewayServerDetailMapper, GatewayServerDetail> implements GatewayServerDetailService {

    @Transactional
    @Override
    public boolean registerGatewayServerNode(String groupId, String gatewayId, String gatewayName, String gatewayAddress) {
        LambdaQueryWrapper<GatewayServerDetail> lambda = new LambdaQueryWrapper<>();
        lambda.eq(GatewayServerDetail::getGatewayId, gatewayId);
        lambda.eq(GatewayServerDetail::getGatewayAddress, gatewayAddress);
        GatewayServerDetail gatewayServerDetail = baseMapper.selectOne(lambda);
        GatewayServerDetail detail = null;
        boolean flag = true;
        // 如果没有就注册，如果有就修改
        if (gatewayServerDetail == null) {
            try {
                detail = new GatewayServerDetail();
                detail.setGatewayId(gatewayId);
                detail.setGatewayAddress(gatewayAddress);
                detail.setGatewayName(gatewayName);
                detail.setGroupId(groupId);
                detail.setStatus(Constants.GatewayStatus.Available);
                baseMapper.insert(detail);
            } catch (DuplicateKeyException e) {
                flag = false;
            }
        } else {
            flag = false;
        }
        if (!flag) {
            // 注册失败，重复了，那么就修改
            LambdaQueryWrapper<GatewayServerDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(GatewayServerDetail::getGatewayId, gatewayId).eq(GatewayServerDetail::getGatewayAddress, gatewayAddress);
            gatewayServerDetail = getOne(lambdaQueryWrapper);
            if (gatewayServerDetail != null) {
                gatewayServerDetail.setStatus(Constants.GatewayStatus.Available);
                updateById(gatewayServerDetail);
            }
        }
        return flag;
    }

    @Override
    public OperationResult<GatewayServerDetail> queryGatewayServerDetail(OperationRequest<GatewayServerDetail> request) {
        List<GatewayServerDetail> gatewayServerDetails = baseMapper.queryGatewayServerDetail(request);
        return new OperationResult<>(gatewayServerDetails.size(), gatewayServerDetails);
    }
}
