package com.xiaohe.gateway.center.service.impl;

import com.xiaohe.gateway.center.message.Publisher;
import com.xiaohe.gateway.center.service.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Resource
    private Publisher publisher;
    @Value("${spring.redis.username}")
    private String username;
    @Value("${spring.redis.password}")
    private String password;

    @Override
    public Map<String, String> queryRedisConfig() {
        return new HashMap<String, String>() {{
            put("host", host);
            put("port", String.valueOf(port));
            put("username", username);
            put("password", password);
        }};
    }

    @Override
    public void pushMessage(String gatewayId, Object message) {
        publisher.pushMessage(gatewayId, message);
    }

}
