package com.xiaohe.gateway.rpc.iml;

import com.alibaba.fastjson.JSON;
import com.xiaohe.gateway.rpc.IActivityBooth;
import com.xiaohe.gateway.rpc.dto.XReq;
import com.xiaohe.gateway.sdk.annotation.ApiProducerClazz;
import com.xiaohe.gateway.sdk.annotation.ApiProducerMethod;
import org.apache.dubbo.config.annotation.Service;


@Service(version = "1.0.0")
@ApiProducerClazz(
        interfaceName = "活动服务",
        interfaceVersion = "1.0.0"
)
public class ActivityBooth implements IActivityBooth {

    @Override
    @ApiProducerMethod(
            uri = "/sayHi",
            methodName = "sayHi方法",
            httpCommandType = "GET"
    )
    public String sayHi(String str) {
        return "hi " + str + " by api-gateway-test-provider";
    }

    @Override
    public String insert(XReq req) {
        return "hi " + JSON.toJSONString(req) + " by api-gateway-test-provider";
    }

    @Override
    public String test(String str, XReq req) {
        return "hi " + str + JSON.toJSONString(req) + " by api-gateway-test-provider";
    }
}
