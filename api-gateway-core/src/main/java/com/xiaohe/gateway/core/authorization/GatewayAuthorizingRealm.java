package com.xiaohe.gateway.core.authorization;

import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class GatewayAuthorizingRealm extends AuthorizingRealm {
    @Override
    public Class getAuthenticationTokenClass() {
        return GatewayAuthorizingToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 暂时不做授权处理
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        try {
            Claims claims = JwtUtil.decode(((GatewayAuthorizingToken) authenticationToken).getJwt());
            // 验证签发人是否匹配
            if (!authenticationToken.getPrincipal().equals(claims.getSubject())) {
                throw new AuthenticationException("无效令牌");
            }
        } catch (Exception e) {
            throw new AuthenticationException("无效令牌");
        }
        return new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), authenticationToken.getCredentials(), this.getName());
    }
}
