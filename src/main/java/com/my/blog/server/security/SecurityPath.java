package com.my.blog.server.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "security")
@Component
public class SecurityPath {
    // 拦截路径
    private List<String> includePath;

    // 放行路径
    private List<String> excludePath;
}
