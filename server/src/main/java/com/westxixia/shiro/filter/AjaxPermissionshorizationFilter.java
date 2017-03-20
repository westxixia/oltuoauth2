package com.westxixia.shiro.filter;


import com.westxixia.shiro.utils.AjaxWebUtils;
import com.westxixia.shiro.utils.ShiroConstants;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: 自定义权限拦截器
 * </p>
 * <p>
 * Description: 自定义权限拦截器，估计Ajax请求和无权限，则返回JSON字符串,
 * 未登录返回-998 未授权返回-999
 * </p>
 */
public class AjaxPermissionshorizationFilter extends PermissionsAuthorizationFilter {

    /** 日志信息 */
    private static final Logger logger = LoggerFactory.getLogger(AjaxPermissionshorizationFilter.class);

 

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        
        
        //判断是否登录
        if(subject.getPrincipal()==null){
            if(AjaxWebUtils.isAjax(request)){
                logger.info("Ajax 请求未登录{}",mappedValue);
                WebUtils.toHttp(response).setHeader(ShiroConstants.AJAX_UNLOGIN,"true");
                WebUtils.toHttp(response).setStatus(ShiroConstants.AJAX_UNLOGIN_STATUS);
            }else{
              this.saveRequestAndRedirectToLogin(request, response);
            }
        }else{
            if(AjaxWebUtils.isAjax(request)){            
                logger.info("Ajax 请求未授权:{},url:{}",mappedValue,this.getPathWithinApplication(request));
                WebUtils.toHttp(response).setHeader(ShiroConstants.AJAX_UNAUTH,"true");
                WebUtils.toHttp(response).setStatus(ShiroConstants.AJAX_UNAUTH_STATUS);
            }else{
                String unauthorizedUrl = getUnauthorizedUrl();
                if (StringUtils.hasText(unauthorizedUrl)) {
                    
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
        return false;
    }
}