package com.xiaohe.gateway.assist.config;


import com.xiaohe.gateway.assist.application.GatewayApplication;
import com.xiaohe.gateway.assist.service.GatewayCenterService;
import com.xiaohe.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import com.xiaohe.gateway.core.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Configuration
@EnableConfigurationProperties(GatewayServiceProperties.class)
public class GatewayAutoConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayAutoConfig.class);

    @Bean
    public GatewayCenterService registerGatewayService() {
        return new GatewayCenterService();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(GatewayServiceProperties properties, GatewayCenterService gatewayCenterService) {
        // 从 center 拉取 redis 配置
        Map<String, String> redisConfig = gatewayCenterService.queryRedisConfig(properties.getAddress());
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setHostName(redisConfig.get("host"));
        standaloneConfig.setPort(Integer.parseInt(redisConfig.get("port")));
        standaloneConfig.setPassword(redisConfig.get("password"));
        // 默认配置信息
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxWaitMillis(30 * 1000);
        poolConfig.setMinIdle(20);
        poolConfig.setMaxIdle(40);
        poolConfig.setTestWhileIdle(true);
        // 创建 Redis 配置
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofSeconds(2))
                .clientName("api-gateway-assist-redis-" + properties.getGatewayId())
                .usePooling().poolConfig(poolConfig).build();
        // 实例化 Redis 链接对象
        return new JedisConnectionFactory(standaloneConfig, clientConfig);
    }

    /**
     * 创建一个Redis监听器
     * @param gatewayApplication
     * @return
     */
    @Bean
    public MessageListenerAdapter messageListenerAdapter(GatewayApplication gatewayApplication) {
        return new MessageListenerAdapter(gatewayApplication, "receiveMessage");
    }

    /**
     * 将Redis监听器设置到容器中
     * @param properties
     * @param redisConnectionFactory
     * @param messageListenerAdapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(GatewayServiceProperties properties,
                                                   RedisConnectionFactory redisConnectionFactory,
                                                   MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new PatternTopic(properties.getGatewayId()));
        return redisMessageListenerContainer;
    }


    @Bean
    public GatewayApplication gatewayApplication(GatewayServiceProperties properties,
                                                 GatewayCenterService registerGatewayService,
                                                 com.xiaohe.gateway.core.session.Configuration configuration,
                                                 Channel gatewaySocketServerChannel) {
        return new GatewayApplication(properties, registerGatewayService, configuration, gatewaySocketServerChannel);
    }

    /**
     * 注册网关配置对象
     * @param properties
     * @return
     */
    @Bean
    public com.xiaohe.gateway.core.session.Configuration gatewayCoreConfiguration(GatewayServiceProperties properties) {
        com.xiaohe.gateway.core.session.Configuration configuration = new com.xiaohe.gateway.core.session.Configuration();
        String[] split = properties.getGatewayAddress().split(":");
        configuration.setHostName(split[0].trim());
        configuration.setPort(Integer.parseInt(split[1]));
        return configuration;
    }

    /**
     * 初始化网关服务，创建服务端 Channel 对象，方便获取和控制网关操作
     * @param configuration
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Bean
    public Channel initGateway(com.xiaohe.gateway.core.session.Configuration configuration) throws ExecutionException, InterruptedException {
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);
        GatewaySocketServer server = new GatewaySocketServer(configuration, gatewaySessionFactory);
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel) throw new RuntimeException("api gateway core netty server start error channel is null");
        while (!channel.isActive()) {
            Thread.sleep(500);
        }
        logger.info("api-gateway-core netty server start! {}", channel.localAddress());
        return channel;
    }


}
