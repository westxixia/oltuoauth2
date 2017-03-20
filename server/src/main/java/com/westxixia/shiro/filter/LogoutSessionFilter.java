package com.westxixia.shiro.filter;


import com.westxixia.shiro.cache.SessionCacheManager;
import com.westxixia.shiro.realm.ShiroUser;
import com.westxixia.shiro.session.LoginSecurityUtil;
import com.westxixia.shiro.utils.ShiroConstants;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>
 * Title: 扩展Logout退出功能
 * </p>
 * <p>
 * Description: 扩展Logout退出功能，并清除Session并发控制缓存中的数据
 * </p>
 */
public class LogoutSessionFilter extends LogoutFilter {
    /** Shiro Session缓存管理*/
    private SessionCacheManager sessionCacheManager;

    /**
     * @return   Shiro Session缓存管理
     */
    public SessionCacheManager getSessionCacheManager() {
        return this.sessionCacheManager;
    }

    /**
     * @param sessionCacheManager  Shiro Session缓存管理
     */
    public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
        this.sessionCacheManager = sessionCacheManager;
    }

    /***
     * 默认构造函数
     */
    public LogoutSessionFilter() {
        super();
    }

    /***
     * 重写该方法，并除缓存的数据
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response)
            throws Exception {
        ShiroUser user = LoginSecurityUtil.getUser();
        if(user!=null){
            //清除缓存
            this.sessionCacheManager.removeSessionController(ShiroConstants.CACHE_SESSION_NAME,user.getUserName());
        }
        return super.preHandle(request, response);
    }
}