package com.xiaohe.gateway.assist;

public class GatewayException extends RuntimeException {
    public GatewayException(String message) {
        super(message);
    }
    public GatewayException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
