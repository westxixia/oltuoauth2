/**
 * <p>Copyright:Copyright(c) 2016</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.rfs.rfscore.core.utils</p>
 * <p>文件名:ComConst.java</p>
 */
package com.westxixia.shiro.utils;

/**
 * <p>
 * 描述:共通定数类
 * </p>
 * 
 */
public class ShiroConstants {
    private volatile static ShiroConstants shiroConstants;

    private ShiroConstants() {
    }

    public static ShiroConstants getSingleton() {
        if (shiroConstants == null) {
            synchronized (ShiroConstants.class) {
                if (shiroConstants == null) {
                    shiroConstants = new ShiroConstants();
                }
            }
        }
        return shiroConstants;
    }

    /** 登录错误代码 */
    public static final String LOGIN_ERROR_MSG = "login_error";
    
    /** Spring UserRealm的实例名称 */
    public static final String USER_REALM_BEAN = "userRealm";
    
    /** 登录错误参数 */
    public static final String FORCE_LOGOUT = "forcelogout";
    
    /** 用户已登录的错误-2 */
    public static final String LOGIN_MAXS_ERROR = "2";
    
    /** 用户被强制退出 */
    public static final String LOGIN_FORCE_ERROR = "1";
    
    /** 控制并发缓存名称 */
    public static final String CACHE_SESSION_NAME = "shiroSessionController";
    
    /** 密码重试次数缓存 */
    public static final String PASSWORD_RERTY_CACHE = "passwordRetryCache";
    
    /** 授权访问-authc */
    public static final String AUTHC = "authc";
    
    /** 角色-roles */
    public static final String ROLES = "roles";
    
    /** 权限管理-perms */
    public static final String PERMS = "perms";
    
    /** 默认premission字符串-perms[\"{0}\"] */
    public static final String PERMS_STRING = "perms[\"{0}\"]";
    
    /** 角色结构格式-role[{0}] */
    public static final String ROLE_STRING = "roles[{0}]";
    
    /** Ajax 未登录的情况 */
    public static final String AJAX_UNLOGIN = "user-unlogin";
    
    /** Ajax AJAX_UNLOGIN_STATUS 未登录状态 */
    public static final int AJAX_UNLOGIN_STATUS = 998;
    
    /** Ajax 未登录的情况 */
    public static final String AJAX_UNAUTH = "user-unauth";
    
    /** Ajax AJAX_UNLOGIN_STATUS 未登录状态 */
    public static final int AJAX_UNAUTH_STATUS = 999;
    
    /** 密码错误最大重试次数，默认为：5 */
    public static final Integer PASSWORD_RETYR_MAXCOUNT = 5;

}
