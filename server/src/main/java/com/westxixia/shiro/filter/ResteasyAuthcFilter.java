package com.westxixia.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResteasyAuthcFilter extends AccessControlFilter {

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            //1、客户端传入的用户身份
            String username = request.getParameter(PARAM_USERNAME);
            //2、客户端传入的密码身份
            String password = request.getParameter(PARAM_PASSWORD);

            //3、生成Token
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);

            try {
                //4、委托给Realm进行登录
                getSubject(request, response).login(token);
            } catch (UnknownAccountException e) {
                e.printStackTrace();
                onLoginUnknownAccount(response); //5、登录失败
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                onLoginFail(response); //5、登录失败
                return false;
            }
        }
        return true;
    }

    //登录失败时默认返回401状态码
    private void onLoginUnknownAccount(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("{\"status\":\"2\"}");
    }

    //登录失败时默认返回401状态码
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("{\"status\":\"1\"}");
    }
}