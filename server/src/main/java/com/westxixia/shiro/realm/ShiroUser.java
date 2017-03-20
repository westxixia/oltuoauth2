package com.westxixia.shiro.realm;


import java.io.Serializable;
import java.util.Objects;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
    public String id;

    /**
     * 登录名
     */
    public String userName;

    /**
     * 姓名
     */
    public String fullName;

    // XULEI STR
    /**
     * 企业Id
     */
    public String entpId;
    // XULEI END

    public ShiroUser(String id, String loginName, String fullName) {
        this.id = id;
        this.userName = loginName;
        this.fullName = fullName;
    }

    public ShiroUser(String id, String loginName, String fullName, String entpId) {
        this.id = id;
        this.userName = loginName;
        this.fullName = fullName;
        this.entpId = entpId;
    }

    public String getName() {
        return fullName;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return userName;
    }

    /**
     * 重载hashCode,只计算loginName;
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(userName);
    }

    /**
     * 重载equals,只计算loginName;
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ShiroUser other = (ShiroUser) obj;
        if (userName == null) {
            if (other.userName != null) {
                return false;
            }
        } else if (!userName.equals(other.userName)) {
            return false;
        }
        return true;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    // XULEI STR
    public String getEntpId() {
        return entpId;
    }

    public void setEntpId(String entpId) {
        this.entpId = entpId;
    }
    // XULEI END
}