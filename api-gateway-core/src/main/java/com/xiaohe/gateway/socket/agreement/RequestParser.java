package com.xiaohe.gateway.socket.agreement;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * HTTP请求解析器
 * get请求 : 使用 Netty 提供的 QueryStringDecoder 解析
 * post请求 : 判断数据格式，如 json、multipart
 *          - application/json : 拿到内容后使用 JSON.parseObject() 解析
 *          - multipart/form-data : 使用 Netty 提供的 HttpPostRequestDecoder 解析
 */
public class RequestParser {
    private final FullHttpRequest request;

    public RequestParser(FullHttpRequest request) {
        this.request = request;
    }

    public Map<String, Object> parse() {
        // 获取参数类型
        String contentType = getContentType();
        // 获取请求类型
        HttpMethod method = request.method();
        // 如果是get请求，使用Netty提供的QueryStringDecoder就可以获取参数
        if (HttpMethod.GET.equals(method)) {
            Map<String, Object> parameterMap = new HashMap<>();
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().forEach((key, value) -> {
                parameterMap.put(key, value.get(0));
            });
            return parameterMap;
        } else if (HttpMethod.POST.equals(method)) {
            // 如果是post请求，还要判断一下数据的格式
            switch (contentType) {
                case "multipart/form-data":
                    Map<String, Object> parameterMap = new HashMap<>();
                    HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
                    decoder.offer(request);
                    decoder.getBodyHttpDatas().forEach(data -> {
                        Attribute attr = (Attribute) data;
                        try {
                            parameterMap.put(data.getName(), attr.getValue());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                case "application/json":
                    ByteBuf byteBuf = request.content().copy();
                    if (byteBuf.isReadable()) {
                        String jsonContent = byteBuf.toString(StandardCharsets.UTF_8);
                        return JSON.parseObject(jsonContent);
                    }
                    break;
                default:
                    throw new RuntimeException("未实现的协议类型 Content-Type：" + contentType);
            }
        }
        throw new RuntimeException("未实现的请求类型 HttpMethod：" + method);
    }
    private String getContentType() {
        Optional<Map.Entry<String, String>> header = request.headers().entries().stream().filter(
                val -> val.getKey().equals("Content-Type")
        ).findAny();
        Map.Entry<String, String> entry = header.orElse(null);
        assert entry != null;
        String contentType = entry.getValue();
        int idx = contentType.indexOf(";");
        // 看看 application/json;charset=utf-8 是否有分号
        if (idx > 0) {
            return contentType.substring(0, idx);
        } else {
            return contentType;
        }
    }

    /**
     * 简单处理请求路径
     */
    public String getUri() {
        String uri = request.uri();
        int idx = uri.indexOf("?");
        uri = idx > 0 ? uri.substring(0, idx) : uri;
        if (uri.equals("/favicon.ico")) return null;
        return uri;
    }
}
