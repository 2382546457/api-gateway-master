package com.xiaohe.gateway.test;

import com.xiaohe.gateway.core.mapping.HttpCommandType;
import com.xiaohe.gateway.core.mapping.HttpStatement;
import com.xiaohe.gateway.core.session.Configuration;
import com.xiaohe.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import com.xiaohe.gateway.core.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiTest {
    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

//    @Test
//    public void test() throws Exception {
//        Configuration configuration = new Configuration();
//        configuration.addGenericReference("api-gateway-test", "com.xiaohe.gateway.rpc.IActivityBooth", "sayHi");
//
//        GenericReferenceSessionFactoryBuilder builder = new GenericReferenceSessionFactoryBuilder();
//        Future<Channel> future = builder.build(configuration);
//
//        logger.info("服务启动完成 {}", future.get().id());
//
//        Thread.sleep(Long.MAX_VALUE);
//    }

//    @Test
//    public void test_gateway() throws Exception {
//        // 1. 创建配置信息
//        Configuration configuration = new Configuration();
//        // 2. 手写一个http服务，将其注册到配置信息中
//        HttpStatement httpStatement = new HttpStatement(
//                "api-gateway-test",
//                "com.xiaohe.gateway.rpc.IActivityBooth",
//                "sayHi",
//                "/wg/activity/sayHi",
//                HttpCommandType.GET
//        );
//        configuration.addMapper(httpStatement);
//
//        // 3. 基于配置，创建会话工厂
//        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);
//
//        // 4. 创建启动网关服务
//        GatewaySocketServer server = new GatewaySocketServer(gatewaySessionFactory);
//
//        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
//        Channel channel = future.get();
//
//        if (null == channel) {
//            throw new RuntimeException("netty Server start error, channel is null");
//        }
//
//        while (!channel.isActive()) {
//            logger.info("netty server gateway start Ing ...");
//            Thread.sleep(500);
//        }
//        logger.info("netty server gateway start Done! {}", channel.localAddress());
//
//        Thread.sleep(Long.MAX_VALUE);
//
//    }

    @Test
    public void test_gateway() throws Exception {
        // 1. 创建配置信息加载注册
        Configuration configuration = new Configuration();

        configuration.registryConfig(
                "api-gateway-test",
                "zookeeper://localhost:2181",
                "com.xiaohe.gateway.rpc.IActivityBooth",
                "1.0.0"
                );

        HttpStatement httpStatement01 = new HttpStatement(
                "api-gateway-test",
                "com.xiaohe.gateway.rpc.IActivityBooth",
                "sayHi",
                "java.lang.String",
                "/wg/activity/sayHi",
                HttpCommandType.GET,
                false
        );

        HttpStatement httpStatement02 = new HttpStatement(
                "api-gateway-test",
                "com.xiaohe.gateway.rpc.IActivityBooth",
                "insert",
                "com.xiaohe.gateway.rpc.dto.XReq",
                "/wg/activity/insert",
                HttpCommandType.POST,
                true);

        // 将两个请求注册到 Configuration 的 MapperRegistry 中
        configuration.addMapper(httpStatement01);
        configuration.addMapper(httpStatement02);

        // 2. 基于配置创建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        // 3. 创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(configuration, gatewaySessionFactory);

        // 4. 启动网关
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();
        if (null == channel) throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            logger.info("netty server gateway start Ing ...");
            Thread.sleep(500);
        }
        logger.info("netty server gateway start Done! {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);


    }


}
