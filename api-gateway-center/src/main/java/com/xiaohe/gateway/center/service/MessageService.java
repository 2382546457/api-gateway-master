package com.xiaohe.gateway.center.service;

import java.util.Map;

public interface MessageService {
    Map<String, String> queryRedisConfig();

    void pushMessage(String gatewayId, Object message);

}
