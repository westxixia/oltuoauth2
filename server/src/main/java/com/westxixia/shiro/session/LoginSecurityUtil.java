package com.westxixia.shiro.session;

import com.westxixia.shiro.realm.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class LoginSecurityUtil {
    public LoginSecurityUtil() {
    }

    public static ShiroUser getUser() {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.getPrincipal() instanceof ShiroUser?(ShiroUser)subject.getPrincipal():null;
    }
}
