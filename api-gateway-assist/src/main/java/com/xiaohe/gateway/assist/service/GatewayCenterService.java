package com.xiaohe.gateway.assist.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xiaohe.gateway.assist.GatewayException;
import com.xiaohe.gateway.assist.common.Result;
import com.xiaohe.gateway.assist.model.aggregates.ApplicationSystemRichInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GatewayCenterService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayCenterService.class);

    /**
     * 将信息注册到网关中心
     * @param address
     * @param groupId
     * @param gatewayId
     * @param gatewayName
     * @param gatewayAddress
     */
    public void doRegister(String address, String groupId, String gatewayId, String gatewayName, String gatewayAddress) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("groupId", groupId);
        paramMap.put("gatewayId", gatewayId);
        paramMap.put("gatewayName", gatewayName);
        paramMap.put("gatewayAddress", gatewayAddress);

        String resultStr = null;
        try {
            resultStr = HttpUtil.post(address + "/wg/admin/config/registerGateway", paramMap, 5550);
        } catch (Exception e) {
            logger.error("网关服务注册异常，链接资源不可用：{}.", address + "/wg/admin/config/registerGateway");
            throw e;
        }

        Result result = JSON.parseObject(resultStr, Result.class);
        logger.info("向网关注册中心注册网关, gatewayId: {}, gatewayName : {}, gatewayAddress : {}, 注册结果 : {}",
                gatewayId, gatewayName, gatewayAddress, resultStr);
        if (!"0000".equals(result.getCode())) {
            throw new GatewayException("网关服务注册异常 [gatewayId：" + gatewayId + "] 、[gatewayAddress：" + gatewayAddress + "]");
        }
    }

    public ApplicationSystemRichInfo pullApplicationSystemRichInfo(String address, String gatewayId, String systemId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("gatewayId", gatewayId);
        paramMap.put("systemId", systemId);

        String resultStr = null;
        try {
            resultStr = HttpUtil.post(address + "/wg/admin/config/queryApplicationSystemRichInfo", paramMap, 1550);
        } catch (Exception e) {
            logger.error("网关服务拉取异常，链接资源不可用：{}." + address + "/wg/admin/config/queryApplicationSystemRichInfo");
            throw e;
        }
        Result<ApplicationSystemRichInfo> result = JSON.parseObject(resultStr, new TypeReference<Result<ApplicationSystemRichInfo>>(){});
        logger.info("从网关中心拉取应用服务和接口的配置信息到本地完成注册。gatewayId: {}.", gatewayId);
        if (!"0000".equals(result.getCode())) {
            throw new GatewayException("从网关中心拉取应用服务和接口的配置信息到本地出现异常。gateway: " + gatewayId);
        }
        return result.getData();
    }

    /**
     * 从网关中心拉取Redis配置
     * @param address
     * @return
     */
    public Map<String, String> queryRedisConfig(String address) {
        String resultStr = HttpUtil.post(address + "/wg/admin/register/queryRedisConfig", "", 5550);
        Result<Map<String, String>> result = JSON.parseObject(resultStr, new TypeReference<Result<Map<String, String>>>() {});
        if (!"0000".equals(result.getCode()))
            throw new GatewayException("从网关中心拉取Redis配置信息异常");
        return result.getData();
    }

}
