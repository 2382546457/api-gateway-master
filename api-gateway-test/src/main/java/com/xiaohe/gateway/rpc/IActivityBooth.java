package com.xiaohe.gateway.rpc;

import com.xiaohe.gateway.rpc.dto.XReq;

public interface IActivityBooth {

    String sayHi(String str);

    String insert(XReq req);

    String test(String str, XReq req);
}
