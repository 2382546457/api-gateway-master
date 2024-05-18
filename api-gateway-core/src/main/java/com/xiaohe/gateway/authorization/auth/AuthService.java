package com.xiaohe.gateway.authorization.auth;

import com.xiaohe.gateway.authorization.GatewayAuthorizingToken;
import com.xiaohe.gateway.authorization.IAuth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * 认证服务的实现类
 */
public class AuthService implements IAuth {

    private Subject subject;

    public AuthService() {
        // 1. 获取 SecurityManager 工厂，此处使用 shiro.ini 配置文件初始化 SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        this.subject = SecurityUtils.getSubject();
    }

    /**
     * 验证
     * @param id 通信管道id
     * @param token jwt
     * @return
     */
    @Override
    public boolean validate(String id, String token) {
        try {
            subject.login(new GatewayAuthorizingToken(id, token));
            return subject.isAuthenticated();
        } finally {
            subject.logout();
        }
    }

}
