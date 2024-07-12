package com.xiaohe.gateway.core.socket.handlers;

import com.xiaohe.gateway.core.bind.IGenericReference;
import com.xiaohe.gateway.core.executor.result.SessionResult;
import com.xiaohe.gateway.core.session.GatewaySession;
import com.xiaohe.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import com.xiaohe.gateway.core.socket.BaseHandler;
import com.xiaohe.gateway.core.socket.agreement.AgreementConstants;
import com.xiaohe.gateway.core.socket.agreement.GatewayResultMessage;
import com.xiaohe.gateway.core.socket.agreement.RequestParser;
import com.xiaohe.gateway.core.socket.agreement.ResponseParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ProtocolDataHandler extends BaseHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolDataHandler.class);

    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    public ProtocolDataHandler(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        logger.info("网关接收请求【消息】 uri：{} method：{}", request.uri(), request.method());
        try {
            // 1. 解析参数
            RequestParser requestParser = new RequestParser(request);
            String uri = requestParser.getUri();
            Map<String, Object> args = requestParser.parse();

            // 2. 调用会话服务
            GatewaySession gatewaySession = gatewaySessionFactory.openSession(uri);
            IGenericReference reference = gatewaySession.getMapper();
            SessionResult result = reference.$invoke(args);

            // 3. 返回结果
            GatewayResultMessage responseMessage;
            if ("0000".equals(result.getCode())) {
                responseMessage = GatewayResultMessage.buildSuccess(result.getData());
            } else {
                responseMessage = GatewayResultMessage.buildError(AgreementConstants.ResponseCode._404.getCode(), "网关协议调用失败！");
            }
            DefaultFullHttpResponse response = new ResponseParser().parse(responseMessage);
            ctx.writeAndFlush(response);

        } catch (Exception e) {
            e.printStackTrace();
            // 3. 封装返回异常结果
            DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(AgreementConstants.ResponseCode._502.getCode(), "网关协议调用失败！" + e.getMessage()));
            channel.writeAndFlush(response);
        }
    }
}
