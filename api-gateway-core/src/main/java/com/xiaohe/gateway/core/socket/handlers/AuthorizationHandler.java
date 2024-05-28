package com.xiaohe.gateway.core.socket.handlers;

import com.xiaohe.gateway.core.mapping.HttpStatement;
import com.xiaohe.gateway.core.session.Configuration;
import com.xiaohe.gateway.core.socket.agreement.ResponseParser;
import com.xiaohe.gateway.core.socket.BaseHandler;
import com.xiaohe.gateway.core.socket.agreement.AgreementConstants;
import com.xiaohe.gateway.core.socket.agreement.GatewayResultMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 鉴权
 */
public class AuthorizationHandler extends BaseHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(AuthorizationHandler.class);

    private final Configuration configuration;

    public AuthorizationHandler(Configuration configuration) {
        this.configuration = configuration;
    }



    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        logger.info("鉴权Hanlder, uri : {}, method : {}.", request.uri(), request.method());
        // 从channel获取HttpStatement
        HttpStatement httpStatement = channel.attr(AgreementConstants.HTTP_STATEMENT).get();
        // 不需要鉴权直接跳过
        if (!httpStatement.isAuth()) {
            request.retain();
            ctx.fireChannelRead(request);
            return;
        }
        try {
            String uId = request.headers().get("uId");
            String token = request.headers().get("token");
            if (token == null || "".equals(token)) {
                DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(AgreementConstants.ResponseCode._400.getCode(), "对不起，你的token不合法"));
                channel.writeAndFlush(response);
            }
            // 鉴权
            boolean status = configuration.authValidate(uId, token);
            if (status) {
                request.retain();
                ctx.fireChannelRead(request);
            } else {
                DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(AgreementConstants.ResponseCode._403.getCode(), "对不起，你无权访问此接口！"));
                channel.writeAndFlush(response);
            }
        } catch (Exception e) {
            DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(AgreementConstants.ResponseCode._403.getCode(), "对不起，你的鉴权不合法！"));
            channel.writeAndFlush(response);
        }

    }
}
