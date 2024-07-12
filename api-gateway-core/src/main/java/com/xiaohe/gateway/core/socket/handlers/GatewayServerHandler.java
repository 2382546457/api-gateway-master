package com.xiaohe.gateway.core.socket.handlers;

import com.xiaohe.gateway.core.mapping.HttpStatement;
import com.xiaohe.gateway.core.session.Configuration;
import com.xiaohe.gateway.core.socket.agreement.ResponseParser;
import com.xiaohe.gateway.core.socket.BaseHandler;
import com.xiaohe.gateway.core.socket.agreement.AgreementConstants;
import com.xiaohe.gateway.core.socket.agreement.GatewayResultMessage;
import com.xiaohe.gateway.core.socket.agreement.RequestParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GatewayServerHandler extends BaseHandler<FullHttpRequest> {
    private final Logger logger = LoggerFactory.getLogger(GatewayServerHandler.class);

    private final Configuration configuration;

    public GatewayServerHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        logger.info("网关接收请求, uri: {}, method: {}", request.uri(), request.method());

        try {
            // 1. 解析参数
            RequestParser requestParser = new RequestParser(request);
            String uri = requestParser.getUri();
            if (uri == null) return ;


            // 2. 保存信息, 将 HttpStatement 保存在 Channel 中
            HttpStatement httpStatement = configuration.getHttpStatement(uri);
            channel.attr(AgreementConstants.HTTP_STATEMENT).set(httpStatement);

            // 3. 放行请求
            request.retain();
            ctx.fireChannelRead(request);
        } catch (Exception e) {
            // 4. 封装返回结果
            DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(AgreementConstants.ResponseCode._500.getCode(), "网关协议调用失败！" + e.getMessage()));
            channel.writeAndFlush(response);
        }
    }
}
