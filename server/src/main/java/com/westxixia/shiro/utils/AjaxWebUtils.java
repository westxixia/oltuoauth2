package com.westxixia.shiro.utils;


import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class AjaxWebUtils {
    public static final String AJAX_XHR = "XMLHttpRequest";
    public static final String AJAX_HEADER_XRW = "x-requested-with";

    public AjaxWebUtils() {
    }

    public static boolean isAjax(ServletRequest request) {
        HttpServletRequest servletRequest = (HttpServletRequest)request;
        return "XMLHttpRequest".equalsIgnoreCase(servletRequest.getHeader("x-requested-with"));
    }
}