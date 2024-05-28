package com.xiaohe.gateway.center;

import com.alibaba.fastjson.JSON;
import com.xiaohe.gateway.center.model.vo.ApplicationSystemRichInfo;
import com.xiaohe.gateway.center.service.ApplicationSystemService;
import com.xiaohe.gateway.center.service.GatewayServerDetailService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class APITest {

    private final static Logger logger = LoggerFactory.getLogger(APITest.class);

    @Resource
    private GatewayServerDetailService gatewayServerDetailService;

    @Resource
    private ApplicationSystemService applicationSystemService;

    @Test
    public void test_registerGatewayServerNode() {
        gatewayServerDetailService.registerGatewayServerNode("10001",
                "api-gateway-g4",
                "电商支付网关",
                "172.20.10.12:7399");
    }

    @Test
    public void test_queryApplicationSystemRichInfo() {
        ApplicationSystemRichInfo result = applicationSystemService.queryAapplicationSystemRichInfo("api-gateway-g4", "");
        logger.info("测试结果：{}", JSON.toJSONString(result));
    }
}
