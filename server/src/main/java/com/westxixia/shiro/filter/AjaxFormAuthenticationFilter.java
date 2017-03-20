package com.westxixia.shiro.filter;


import com.westxixia.shiro.utils.AjaxWebUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>
 * Title: 扩展Shiro的FormAuthenticationFilter,用于拦截处理Ajax的请求
 * </p>
 * <p>
 * Description: 针对Ajax请求登录请求情况，拦截信息并返回998的状态
 * </p>
 */
public class AjaxFormAuthenticationFilter extends FormAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(AjaxFormAuthenticationFilter.class);

    public AjaxFormAuthenticationFilter() {
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if(this.isLoginRequest(request, response)) {
            if(this.isLoginSubmission(request, response)) {
                logger.trace("Login submission detected.  Attempting to execute login.");
                return this.executeLogin(request, response);
            } else {
                if(AjaxWebUtils.isAjax(request)) {
                    logger.trace("Ajax登录请求未登录,拦截{}", this.getPathWithinApplication(request));
                    WebUtils.toHttp(response).setHeader("user-unlogin", "true");
                    WebUtils.toHttp(response).setStatus(998);
                }

                logger.trace("Login page view.");
                return true;
            }
        } else {
            if(AjaxWebUtils.isAjax(request)) {
                logger.info("Ajax 请求未登录{}", this.getPathWithinApplication(request));
                WebUtils.toHttp(response).setHeader("user-unlogin", "true");
                WebUtils.toHttp(response).setStatus(998);
            } else {
                logger.trace("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + this.getLoginUrl() + "]");
                this.saveRequestAndRedirectToLogin(request, response);
            }

            return false;
        }
    }
}
