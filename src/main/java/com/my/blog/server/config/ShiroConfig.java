package com.my.blog.server.config;

import com.my.blog.server.filter.LoginFilter;
import com.my.blog.server.security.LoginRealm;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {
    @Resource
    private LoginRealm loginRealm;

    @Resource
    private LoginFilter loginFilter;

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 添加自定义处理Realm
        defaultWebSecurityManager.setRealm(loginRealm);

        // 设置 Subject和Session联系
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();

        DefaultWebSessionStorageEvaluator defaultWebSessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        // 禁用session
        defaultWebSessionStorageEvaluator.setSessionStorageEnabled(false);
        defaultSubjectDAO.setSessionStorageEvaluator(defaultWebSessionStorageEvaluator);

        defaultWebSecurityManager.setSubjectDAO(defaultSubjectDAO);
        return defaultWebSecurityManager;
    }

    @Bean
    public DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
        // 配置过滤器拦截规则
        // 所有请求都走过滤器
        defaultShiroFilterChainDefinition.addPathDefinition("/**", LoginFilter.Filter_NAME);
        return defaultShiroFilterChainDefinition;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager, DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        // 添加过滤器
        LinkedHashMap<String, Filter> filterLinkedHashMap = new LinkedHashMap<>();
        filterLinkedHashMap.put(LoginFilter.Filter_NAME, loginFilter);
        shiroFilterFactoryBean.setFilters(filterLinkedHashMap);

        // 添加过滤器拦截规则
        shiroFilterFactoryBean.setFilterChainDefinitionMap(defaultShiroFilterChainDefinition.getFilterChainMap());

        return shiroFilterFactoryBean;
    }
}
