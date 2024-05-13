package com.xiaohe.gateway.session;

import io.netty.channel.Channel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 泛化调用 会话 工厂
 */
public interface IGenericReferenceSessionFactory {

    Future<Channel> openSession() throws ExecutionException, InterruptedException;
}
