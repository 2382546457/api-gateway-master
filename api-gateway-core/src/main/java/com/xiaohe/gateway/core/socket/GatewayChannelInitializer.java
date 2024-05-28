package com.xiaohe.gateway.core.socket;

import com.xiaohe.gateway.core.session.Configuration;
import com.xiaohe.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import com.xiaohe.gateway.core.socket.handlers.AuthorizationHandler;
import com.xiaohe.gateway.core.socket.handlers.GatewayServerHandler;
import com.xiaohe.gateway.core.socket.handlers.ProtocolDataHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class GatewayChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final DefaultGatewaySessionFactory gatewaySessionFactory;
    private final Configuration configuration;

    public GatewayChannelInitializer(DefaultGatewaySessionFactory gatewaySessionFactory, Configuration configuration) {
        this.gatewaySessionFactory = gatewaySessionFactory;
        this.configuration = configuration;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));

        pipeline.addLast(new GatewayServerHandler(configuration));
        pipeline.addLast(new AuthorizationHandler(configuration));
        pipeline.addLast(new ProtocolDataHandler(gatewaySessionFactory));
    }
}
