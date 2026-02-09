package com.my.blog.server.security;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.github.benmanes.caffeine.cache.Cache;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminUserLoginException;
import com.my.blog.common.utils.JWTUtils;
import com.my.blog.pojo.po.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginRealm extends AuthenticatingRealm {
    @Resource
    private JWTUtils jwtUtils;

    @Resource
    private Cache<String,String> tokenCache;
    @Override
    public boolean supports(AuthenticationToken token) {
        // 只处理自定义认证令牌
        return token instanceof JWTToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) authenticationToken;
        String token= (String) jwtToken.getCredentials();

        // 获取登录用户id
        Long userId = jwtUtils.getUserId(token);

        if(userId==null) {
            throw new AdminUserLoginException(ExceptionEnums.ADMIN_USER_NOT_LOGIN);
        }

        // redis令牌再校验
        String cacheToken = tokenCache.getIfPresent("user:token:" + userId);
        if(cacheToken==null) {
            throw new AdminUserLoginException(ExceptionEnums.ADMIN_USER_LOGIN_TIMEOUT);
        }
        if(!cacheToken.equals(token)) {
            throw new AdminUserLoginException(ExceptionEnums.ADMIN_USER_LOGIN_TIMEOUT);
        }

        // 获取用户数据
        User user = Db.lambdaQuery(User.class).eq(User::getId, userId).one();

        return new SimpleAuthenticationInfo(user, token, getName());
    }
}
