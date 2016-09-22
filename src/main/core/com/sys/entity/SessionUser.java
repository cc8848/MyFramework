package com.sys.entity;

/**
 * session中的用户信息
 * @author zzl
 * Date:2014-08-30
 */
public class SessionUser {


    private String userId;
    private String userName;
    private String realName;
    private Integer userType;
    private SysDepEntity dep;

    public SessionUser() {
    }

    public SessionUser(SysUserEntity userEntity) {
        if(userEntity==null) return ;
        this.userId = userEntity.getId();
        this.userName=userEntity.getUsername();
        this.realName=userEntity.getRealname();
        this.userType=userEntity.getUserType();
        this.dep=userEntity.getDep();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public SysDepEntity getDep() {
        return dep;
    }

    public void setDep(SysDepEntity dep) {
        this.dep = dep;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
