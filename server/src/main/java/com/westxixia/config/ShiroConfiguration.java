package com.westxixia.config;

import com.westxixia.shiro.cache.SessionCacheManager;
import com.westxixia.shiro.credentials.RetryLimitHashedCredentialsMatcher;
import com.westxixia.shiro.filter.AjaxFormAuthenticationFilter;
import com.westxixia.shiro.filter.AjaxPermissionshorizationFilter;
import com.westxixia.shiro.filter.ForceLogoutFilter;
import com.westxixia.shiro.filter.LogoutSessionFilter;
import com.westxixia.shiro.filter.ResteasyAuthcFilter;
import com.westxixia.shiro.filter.SessionControllerFilter;
import com.westxixia.shiro.realm.UserRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

    private static Map<String, Filter> filters = new LinkedHashMap();

    /**
     * 缓存管理器 使用Ehcache实现
     *
     * @return
     */
    @Bean(name = "cacheManagerShiro")
    public EhCacheManager getCacheManagerShiro() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache-shiro.xml");
        return ehCacheManager;
    }

    /**
     * 密码凭证匹配器
     *
     * @return
     */

    @Bean(name = "retryLimitHashedCredentialsMatcher")
    public RetryLimitHashedCredentialsMatcher getRetryLimitHashedCredentialsMatcher() {
        RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher
                (getCacheManagerShiro());
        retryLimitHashedCredentialsMatcher.setHashAlgorithmName("MD5");
        retryLimitHashedCredentialsMatcher.setHashIterations(1);
        retryLimitHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        retryLimitHashedCredentialsMatcher.setRetryMaxCount(3);
        return retryLimitHashedCredentialsMatcher;
    }

    /**
     * 用户Realm实现
     *
     * @return
     */
    @Bean(name = "userRealm")
    public UserRealm getUserRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(getRetryLimitHashedCredentialsMatcher());
        userRealm.setCachingEnabled(true);
        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthenticationCacheName("authenticationCache");
        userRealm.setAuthorizationCachingEnabled(true);
        userRealm.setAuthorizationCacheName("authorizationCache");
        return userRealm;
    }

    /**
     * 会话ID生成器
     *
     * @return
     */
    @Bean(name = "sessionIdGenerator")
    public JavaUuidSessionIdGenerator getSessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 会话Cookie模板
     *
     * @return
     */
    @Bean(name = "sessionIdCookie")
    public SimpleCookie getSessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(180000);
        return simpleCookie;
    }

    /**
     * 记住登录状态
     *
     * @return
     */
    @Bean(name = "rememberMeCookie")
    public SimpleCookie getRememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(604800); // 1天
        simpleCookie.setPath("path");
        return simpleCookie;
    }

    /**
     * rememberMe管理器
     *
     * @return
     */
    @Bean(name = "rememberMeManager")
    public CookieRememberMeManager getRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        cookieRememberMeManager.setCookie(getRememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * 会话DAO
     *
     * @return
     */
    @Bean(name = "sessionDAO")
    public EnterpriseCacheSessionDAO getSessionDAO() {
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        enterpriseCacheSessionDAO.setSessionIdGenerator(getSessionIdGenerator());
        return enterpriseCacheSessionDAO;
    }

    /**
     * @return
     */
    @Bean(name = "sessionValidationScheduler")
    public QuartzSessionValidationScheduler getSessionValidationScheduler() {
        QuartzSessionValidationScheduler quartzSessionValidationScheduler = new QuartzSessionValidationScheduler();
        quartzSessionValidationScheduler.setSessionValidationInterval(1800000);
        quartzSessionValidationScheduler.setSessionManager(getSessionManager());
        return quartzSessionValidationScheduler;
    }

    /**
     * 会话管理器
     *
     * @return
     */
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager getSessionManager() {
        DefaultWebSessionManager defaultAdvisorAutoProxyCreator = new DefaultWebSessionManager();
        defaultAdvisorAutoProxyCreator.setGlobalSessionTimeout(1800000);
        defaultAdvisorAutoProxyCreator.setDeleteInvalidSessions(true);
        defaultAdvisorAutoProxyCreator.setSessionValidationSchedulerEnabled(true);
        defaultAdvisorAutoProxyCreator.setSessionValidationScheduler(getSessionValidationScheduler());
        defaultAdvisorAutoProxyCreator.setSessionDAO(getSessionDAO());
        defaultAdvisorAutoProxyCreator.setSessionIdCookieEnabled(true);
        defaultAdvisorAutoProxyCreator.setSessionIdCookie(getSessionIdCookie());
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 安全管理器
     *
     * @return
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(getUserRealm());
        defaultWebSecurityManager.setSessionManager(getSessionManager());
        defaultWebSecurityManager.setCacheManager(getCacheManagerShiro());
        defaultWebSecurityManager.setRememberMeManager(getRememberMeManager());
        return defaultWebSecurityManager;
    }

    /**
     * 相当于调用SecurityUtils.setSecurityManager(securityManager)
     *
     * @return
     */
    @Bean(name = "methodInvokingFactoryBean")
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(new Object[]{getSecurityManager()});
        return methodInvokingFactoryBean;
    }

    /**
     * 基于Form表单的身份验证过滤器
     *
     * @return
     */
    @Bean(name = "formAuthenticationFilter")
    public AjaxFormAuthenticationFilter getFormAuthenticationFilter() {
        AjaxFormAuthenticationFilter ajaxFormAuthenticationFilter = new AjaxFormAuthenticationFilter();
        ajaxFormAuthenticationFilter.setRememberMeParam("rememberMe");
        ajaxFormAuthenticationFilter.setUsernameParam("username");
        ajaxFormAuthenticationFilter.setPasswordParam("password");
        ajaxFormAuthenticationFilter.setLoginUrl("/login");
        ajaxFormAuthenticationFilter.setSuccessUrl("/index");
        return ajaxFormAuthenticationFilter;
    }

    /**
     * 强制用户退出
     *
     * @return
     */
    @Bean(name = "forceLogoutFilter")
    public ForceLogoutFilter getForceLogoutFilter() {
        ForceLogoutFilter forceLogoutFilter = new ForceLogoutFilter();
        forceLogoutFilter.setForceLogoutUrl("/login");
        forceLogoutFilter.setSessionCacheManager(getSessionCacheManager());
        return forceLogoutFilter;
    }

    /**
     * 用户会话并发控制
     *
     * @return
     */
    @Bean(name = "sessionControlFilter")
    public SessionControllerFilter getSessionControlFilter() {
        SessionControllerFilter sessionControllerFilter = new SessionControllerFilter();
        sessionControllerFilter.setSessionManager(getSessionManager());
        sessionControllerFilter.setMaxSession(1);
        sessionControllerFilter.setForceLogoutUrl("/login");
        sessionControllerFilter.setForceLogoutAfter(false);
        return sessionControllerFilter;
    }

    /**
     * Shiro Session Cache管理
     *
     * @return
     */
    @Bean(name = "sessionCacheManager")
    public SessionCacheManager getSessionCacheManager() {
        SessionCacheManager sessionCacheManager = new SessionCacheManager();
        sessionCacheManager.setCacheManager(getCacheManagerShiro());
        return sessionCacheManager;
    }

    /**
     * logout退出过滤器
     *
     * @return
     */
    @Bean(name = "logoutFilter")
    public LogoutSessionFilter getLogoutFilter() {
        LogoutSessionFilter logoutSessionFilter = new LogoutSessionFilter();
        logoutSessionFilter.setRedirectUrl("/login");
        logoutSessionFilter.setSessionCacheManager(getSessionCacheManager());
        return logoutSessionFilter;
    }

    /**
     * Ajax登录及权限过滤
     *
     * @return
     */
    @Bean(name = "ajaxPermissions")
    public AjaxPermissionshorizationFilter getAjaxPermissions() {
        return new AjaxPermissionshorizationFilter();
    }

    /**
     * Resteasy登录应用
     *
     * @return
     */
    @Bean(name = "resteasyAuthcFilter")
    public ResteasyAuthcFilter getResteasyAuthcFilter() {
        return new ResteasyAuthcFilter();
    }


    /**
     * Shiro的Web过滤器
     *
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean
                .setSecurityManager(getSecurityManager());
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        filters.put("authc", getFormAuthenticationFilter());
        filters.put("forceLogoutFilter", getForceLogoutFilter());
        filters.put("logoutFilter", getLogoutFilter());
        filters.put("perms", getAjaxPermissions());
        filters.put("kickout", getSessionControlFilter());
        filters.put("resteasyAuthc", getResteasyAuthcFilter());
        filterChainDefinitionMap.put("/logout", "logoutFilter");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/api/**", "user");
        filterChainDefinitionMap.put("/resteasy/**", "resteasyAuthc");
        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
        filterChainDefinitionMap.put("/**", "user,kickout");
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * Shiro生命周期处理器
     *
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
