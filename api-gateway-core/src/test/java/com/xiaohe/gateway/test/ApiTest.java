package com.xiaohe.gateway.test;

import com.xiaohe.gateway.session.SessionServer;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiTest {
    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test() throws Exception {
        SessionServer server = new SessionServer();

        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);

        Channel channel = future.get();

        if (channel == null) {
            throw new RuntimeException("netty server start error channel is null");
        }
        while (!channel.isActive()) {
            logger.info("NettyServer 启动服务...");
            Thread.sleep(500);
        }
        logger.info("NettyServer 启动服务完成 {}.", channel.localAddress());
        Thread.sleep(Long.MAX_VALUE);
    }

}
