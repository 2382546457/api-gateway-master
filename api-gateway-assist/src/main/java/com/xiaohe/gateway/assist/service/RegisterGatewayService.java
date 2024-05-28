package com.xiaohe.gateway.assist.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.xiaohe.gateway.assist.GatewayException;
import com.xiaohe.gateway.assist.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RegisterGatewayService {
    private Logger logger = LoggerFactory.getLogger(RegisterGatewayService.class);

    public void doRegister(String address, String groupId, String gatewayId, String gatewayName, String gatewayAddress) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("groupId", groupId);
        paramMap.put("gatewayId", gatewayId);
        paramMap.put("gatewayName", gatewayName);
        paramMap.put("gatewayAddress", gatewayAddress);

        String resultStr = HttpUtil.post(address, paramMap, 350);

        Result result = JSON.parseObject(resultStr, Result.class);
        logger.info("向网关注册中心注册网关, gatewayId: {}, gatewayName : {}, gatewayAddress : {}, 注册结果 : {}",
                gatewayId, gatewayName, gatewayAddress, resultStr);
        if (!"0000".equals(result.getCode())) {
            throw new GatewayException("网关服务注册异常 [gatewayId：" + gatewayId + "] 、[gatewayAddress：" + gatewayAddress + "]");
        }
    }
}
