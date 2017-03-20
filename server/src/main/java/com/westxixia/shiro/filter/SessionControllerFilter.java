package com.westxixia.shiro.filter;


import com.westxixia.shiro.realm.ShiroUser;
import com.westxixia.shiro.utils.ShiroConstants;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * <p>
 * Title: 控制session 并发登录人数过滤器
 * </p>
 * <p>
 * Description: 控制session 并发登录人数过滤器
 * </p>
 */
public class SessionControllerFilter extends AccessControlFilter {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(SessionControllerFilter.class);
    /**
     * 强制退出的KEY
     */
    private static final String FORCE_LOGOUT_KEY = SessionControllerFilter.class.getName() + ".killForceLogout";

    /**
     * 后用户将强制退出的URL
     */
    private String forceLogoutUrl;
    /**
     * 强制退出之前或之后用户
     */
    private boolean forceLogoutAfter = Boolean.TRUE;
    /**
     * 单一个用户登录会话数量,默认为1
     */
    private int maxSession = 1;
    /**
     * Shiro 会话管理
     */
    private SessionManager sessionManager;
    /**
     * Shiro 缓存管理
     */
    private CacheManager cacheManager;

    /**
     * 用户与SessionID 关联
     */
    private Cache<String, Deque<Serializable>> cache;

    /***
     * 默认构造函数
     */
    public SessionControllerFilter() {
        super();
    }


    /***
     * 默认构造函数
     */
    public SessionControllerFilter(CacheManager cacheManager) {
        super();
        cache = cacheManager.getCache(ShiroConstants.CACHE_SESSION_NAME);
    }

    /**
     * @return 后用户将强制退出的URL
     */
    public String getForceLogoutUrl() {
        return forceLogoutUrl;
    }

    /**
     * @param forceLogoutUrl 后用户将强制退出的URL
     */
    public void setForceLogoutUrl(String forceLogoutUrl) {
        this.forceLogoutUrl = forceLogoutUrl;
    }

    /**
     * @return the forceLogoutAfter 强制退出之前或之后用户
     */
    public boolean isForceLogoutAfter() {
        return forceLogoutAfter;
    }

    /**
     * @param forceLogoutAfter the  强制退出之前或之后用户
     */
    public void setForceLogoutAfter(boolean forceLogoutAfter) {
        this.forceLogoutAfter = forceLogoutAfter;
    }

    /**
     * @return 会话数量 默认为1
     */
    public int getMaxSession() {
        return maxSession;
    }

    /**
     * @param maxSession 会话数量
     */
    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    /**
     * @return the sessionManager Shiro 会话管理
     */
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    /**
     * @param sessionManager Shiro 会话管理
     */
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    /**
     * @return 缓存管理
     */
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * @param cacheManager 缓存管理
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


    /* (non-Javadoc)
     * @see org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    /* (non-Javadoc)
     * @see org.apache.shiro.web.filter.AccessControlFilter#onAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        //如果没有登录，直接进行之后的流程
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }
        Session session = subject.getSession();
        ShiroUser user = (ShiroUser) subject.getPrincipal();
        Serializable sessionId = session.getId();
        //同步控制
        Deque<Serializable> deque = cache.get(user.getUserName());
        if (deque == null) {
            deque = new LinkedList<Serializable>();
            cache.put(user.getUserName(), deque);
        }

        //如果队列里没有此sessionId，且用户没有被强制退出；放入队列
        if (!deque.contains(sessionId) && session.getAttribute(FORCE_LOGOUT_KEY) == null) {
            deque.push(sessionId);
            //cache.put(user.getUsername(), deque);
        }

        //如果队列里的sessionId数超出最大会话数，开始强制退用户
        while (deque.size() > maxSession) {
            //forceLogoutAfter==true,如果强制退出后者,否则强制退出前者
            Serializable killSessionId = forceLogoutAfter ? deque.removeFirst() : deque.removeLast();
            try {
                Session killSession = sessionManager.getSession(new DefaultSessionKey(killSessionId));
                if (killSession != null) {
                    //设置会话的FORCE_LOGOUT_KEY属性表示强制退出
                    killSession.setAttribute(FORCE_LOGOUT_KEY, true);
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }

        //如果被会话被强制退出了，重定向到强制退出的URL
        if (session.getAttribute(FORCE_LOGOUT_KEY) != null) {
            //会话被强制退出
            try {
                subject.logout();
            } catch (Exception e) {
                logger.error("", e);
            }
            saveRequest(request);
            StringBuilder sb = new StringBuilder();
            sb.append(this.forceLogoutUrl);
            sb.append(this.forceLogoutUrl.contains("?") ? "&" : "?");
            sb.append(ShiroConstants.FORCE_LOGOUT);
            sb.append("=");
            sb.append(ShiroConstants.LOGIN_MAXS_ERROR);
            WebUtils.issueRedirect(request, response, sb.toString());
            return false;
        }
        return true;
    }
}