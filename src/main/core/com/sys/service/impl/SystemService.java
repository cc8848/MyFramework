package com.sys.service.impl;

import com.sys.dao.IBaseCommonDao;
import com.sys.entity.SessionUser;
import com.sys.entity.SysDepEntity;
import com.sys.service.ISystemService;
import com.sys.util.WebUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zzl
 *         Date:2014-07-30
 */
@Service("systemService")
@Transactional
public class SystemService  implements ISystemService{
    @Resource
    private IBaseCommonDao baseCommonDao;

    public void addLog(String msg, String oType) {

    }

    private SessionUser getSessionUser(){
        SessionUser user= WebUtil.getSessionUser();
        if(user==null){
            user=new SessionUser();
            user.setUserId("");
            user.setUserName("未知用户");
            user.setRealName("未知用户");
            user.setDep(new SysDepEntity(0l));
        }
        return user;
    }
}
