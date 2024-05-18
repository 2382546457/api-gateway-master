package com.xiaohe.gateway.socket.handlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xiaohe.gateway.bind.IGenericReference;
import com.xiaohe.gateway.session.GatewaySession;
import com.xiaohe.gateway.session.defaults.DefaultGatewaySessionFactory;
import com.xiaohe.gateway.socket.BaseHandler;
import com.xiaohe.gateway.socket.agreement.RequestParser;
import com.xiaohe.gateway.socket.agreement.ResponseParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GatewayServerHandler extends BaseHandler<FullHttpRequest> {
    private final Logger logger = LoggerFactory.getLogger(GatewayServerHandler.class);

    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    public GatewayServerHandler(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        logger.info("网关接收请求, uri: {}, method: {}", request.uri(), request.method());
        RequestParser requestParser = new RequestParser(request);
        String uri = requestParser.getUri();
        if (uri == null) {
            return;
        }
        Map<String, Object> args = requestParser.parse();
        GatewaySession gatewaySession = gatewaySessionFactory.openSession(uri);
        IGenericReference reference = gatewaySession.getMapper();
        Object result = reference.$invoke(args);

        DefaultFullHttpResponse response = new ResponseParser().parse(result);
        channel.writeAndFlush(response);
    }
}
