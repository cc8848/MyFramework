package com.sys.service;

import com.sys.annotation.Ehcache;
import com.sys.entity.SysUserEntity;
import com.sys.service.ICommonService;

import java.util.List;

public interface SysUserService  extends ICommonService {
    /**
     * 获取角色授权的用户
     * @param roleId
     * @return
     */
    public List<SysUserEntity> getRoleUsers(long roleId);


    /**
     * 通过用户名和密码查询用户信息
     * @param userName
     * @param passwd
     * @return
     */
    public List<SysUserEntity> findByUserNameAndPasswd(String userName, String passwd);

    /**
     * 通过用户秘钥查找用户
     * @param userKey
     * @return
     */
    public SysUserEntity findByUserKey(String userKey);


    /**
     * 根据用户类型获取用户信息
     * @param userType
     * @return
     */
    public List<SysUserEntity> getUsersByType(Integer userType);
}
