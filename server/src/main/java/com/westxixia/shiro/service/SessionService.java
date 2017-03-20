package com.westxixia.shiro.service;


import com.westxixia.shiro.model.SessionModel;

import java.util.List;


/**
 * <p>
 * Title: 用户会话管理服务接口定义
 * </p>
 * <p>
 * Description: 用用户会话管理服务接口定义（获取用户在线会话列表、强制退出会话）
 * </p>
 */
public interface SessionService {
    /***
     * 强制退出的KEY
     */
    String SESSION_FORCE_LOGOUT_KEY = SessionService.class.getName() + ".forceLogout";

    /***
     * 获取当前在线用户列表信息
     *
     * @return List<SessionModel>  用户会话列表
     */
    List<SessionModel> getActiveSessions();

    /***
     * 强制结束用户会话
     *
     * @param sessionId 会话ID
     * @return true ,强制退出成功，false  强制退出失败
     */
    boolean forceLogout(String sessionId);

    /***
     * 根据会话ID读取用户会话
     *
     * @param sessionId sessionId
     * @return SessionModel
     */
    SessionModel getSession(String sessionId);
}
