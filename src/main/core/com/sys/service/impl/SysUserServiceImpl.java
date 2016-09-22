package com.sys.service.impl;

import com.sys.annotation.Ehcache;
import com.sys.entity.SysUserEntity;
import com.sys.service.SysUserService;
import com.sys.service.impl.CommonService;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("sysUserService")
@Transactional
public class SysUserServiceImpl extends CommonService implements SysUserService {
    @Override
    public List<SysUserEntity> getRoleUsers(long roleId) {
        return getDataListByHql("select u from SysUserEntity u,RoleUserEntity r where u.id=r.userId and r.roleId=?",new Object[]{roleId});
    }
    @Override
    public List<SysUserEntity> findByUserNameAndPasswd(String userName, String passwd) {
        return getDataListByHql(" from SysUserEntity where username=? and passwd=?",new Object[]{userName,passwd});
    }

    @Override
    public SysUserEntity findByUserKey(String userKey) {
        List<SysUserEntity> userList=getDataListByHql("from SysUserEntity where userkey=? and state='1' and thirdlogin='1' ",new Object[]{userKey});
        if(CollectionUtils.isNotEmpty(userList))
            return userList.get(0);
        return null;
    }

    /**
     * 根据用户类型获取用户信息
     * @param userType
     * @return
     */
    @Override
    public List<SysUserEntity> getUsersByType(Integer userType) {
        List<Criterion> list = new ArrayList<>();
        list.add(Restrictions.eq("userType",userType));
        List<SysUserEntity> users = this.getDataList(SysUserEntity.class,list,null);

        return users;
    }
}
