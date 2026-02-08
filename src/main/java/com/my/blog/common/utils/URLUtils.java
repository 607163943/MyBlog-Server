package com.my.blog.common.utils;

import com.my.blog.server.security.SecurityPath;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;

@Component
public class URLUtils {
    @Resource
    private SecurityPath securityPath;

    private final AntPathMatcher antPathMatcher=new AntPathMatcher();

    public boolean isExcludePath(String requestURI) {
        // 排除路径
        for (String path : securityPath.getExcludePath()) {
            if (antPathMatcher.match(path, requestURI)) {
                return true;
            }
        }

        // 包含路径
        for (String path : securityPath.getIncludePath()) {
            if (antPathMatcher.match(path, requestURI)) {
                return false;
            }
        }

        // 放行路径
        return true;
    }
}
