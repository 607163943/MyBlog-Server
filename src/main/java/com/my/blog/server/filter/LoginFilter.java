package com.my.blog.server.filter;

import cn.hutool.core.util.StrUtil;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminUserLoginException;
import com.my.blog.common.utils.JWTUtils;
import com.my.blog.common.utils.URLUtils;
import com.my.blog.server.security.JWTToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFilter extends AuthenticatingFilter {

    @Resource
    private JWTUtils jwtUtils;

    @Resource
    private URLUtils urlUtils;

    @Lazy
    @Resource
    private HandlerExceptionResolver handlerExceptionResolver;

    public static final String Filter_NAME = "login_filter";

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        // 获取token
        HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);
        String token = httpServletRequest.getHeader("token");

        // 判空
        if (StrUtil.isEmpty(token)) {
            throw new AdminUserLoginException(ExceptionEnums.ADMIN_USER_NOT_LOGIN);
        }

        // 校验
        try {
            jwtUtils.verifyJWT(token);
        } catch (Exception e) {
            throw new AdminUserLoginException(ExceptionEnums.ADMIN_USER_LOGIN_TIMEOUT);
        }

        return JWTToken.builder()
                .token(token)
                .build();
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        // 异常交给Spring全局异常管理
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        // 业务异常做单独处理
        if(e.getCause() instanceof AdminUserLoginException) {
            AdminUserLoginException adminUserLoginException = (AdminUserLoginException) e.getCause();
            handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, adminUserLoginException);
        } else {
            handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, e);
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 所有拦截请求都走认证
        return executeLogin(servletRequest, servletResponse);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String uri = httpServletRequest.getRequestURI();

        // 根据路径配置判断是否放行
        return urlUtils.isExcludePath(uri);
    }
}
