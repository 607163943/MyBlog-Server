package com.my.blog.server.handler;

import com.my.blog.common.exception.admin.AdminUserLoginException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 最高优先级
public class GlobalFilterExceptionHandler extends OncePerRequestFilter {
    @Resource
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        // 将filter异常链路交给Spring全局异常管理
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 业务异常做单独处理
            if(e.getCause() instanceof AdminUserLoginException) {
                AdminUserLoginException adminUserLoginException = (AdminUserLoginException) e.getCause();
                handlerExceptionResolver.resolveException(request, response, null, adminUserLoginException);
            } else {
                handlerExceptionResolver.resolveException(request, response, null, e);
            }
        }
    }
}
