package com.xiaohe.gateway.session;

import com.xiaohe.gateway.session.handler.SessionServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 用于向 pipeline 注册处理器的实现类
 */
public class SessionChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        // 添加HTTP协议编码、解码器. 注意这里的 RequestDecoder 和 ResponseEncoder
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        // 限制HTTP消息长度
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        // 添加我们自己的处理器
        pipeline.addLast(new SessionServerHandler());
    }
}
