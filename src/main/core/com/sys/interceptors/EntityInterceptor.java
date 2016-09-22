package com.sys.interceptors;

import com.code.entity.BaseEntity;
import com.sys.entity.SessionUser;
import com.sys.entity.SysDepEntity;
import com.sys.util.ResourceUtil;
import com.sys.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
@Component("entityInterceptor")
public class EntityInterceptor extends EmptyInterceptor {
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        try{
            SessionUser user=getSessionUser();
            for(int i=0;i<propertyNames.length;i++){
                if("_createUserId".equals(propertyNames[i])){
                    if(state[i] != null && state[i] != ""){
                        return true;
                    }
                    state[i]=user.getUserId();
                }
                if("_createUserName".equals(propertyNames[i])){
                    state[i]=user.getRealName();
                }
                if("_createDate".equals(propertyNames[i])){
                    state[i]=new Date();
                }
                if("_dataState".equals(propertyNames[i])){
                    state[i]=1;
                }
                if("barCode".equals(propertyNames[i])){
                    if(state[i]==null|| StringUtils.isBlank(state[i].toString()))
                        state[i]=new Date().getTime()+"";
                }
                if("depIdIdentify".equals(propertyNames[i])){
                    state[i]=user.getDep().getId();
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        super.onDelete(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        try{
            boolean update=true;
            for(int i=0;i<propertyNames.length;i++){
                if("_dataState".equals(propertyNames[i])){
                    if(!Objects.equals(2,previousState[i])&&Objects.equals(2,currentState[i])){
                        update=false;
                    }
                }
            }

            if(update){
                doUpdate(propertyNames,currentState,previousState);
            }else{
                doDel(propertyNames,currentState,previousState);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onLoad(entity, id, state, propertyNames, types);
    }

    /**
     * 获取当前用户信息
     * @return
     */
    private SessionUser getSessionUser(){
        SessionUser user=WebUtil.getSessionUser();
        if(user==null){
            user=new SessionUser();
            user.setUserId("402881ce48fdf71c0148fdf955f50001");
            user.setUserName("尚德管理员");
            user.setRealName("尚德管理员");
            user.setDep(new SysDepEntity(1419235298271l));
        }
        return user;
    }

    /**
     * 更新时填充更新时间、更新人等信息
     * @param propertyNames
     * @param currentState
     */
    private void doUpdate(String[] propertyNames,Object[] currentState, Object[] previousState){
        SessionUser user=getSessionUser();
        for(int i=0;i<propertyNames.length;i++){
            if("_updateUserId".equals(propertyNames[i])){
                currentState[i]=user.getUserId();
            }
            if("_updateUserName".equals(propertyNames[i])){
                currentState[i]=user.getRealName();
            }
            if("_updateDate".equals(propertyNames[i])){
                currentState[i]=new Date();
            }
            if("_dataState".equals(propertyNames[i])){
                currentState[i]=1;
            }
            if("depIdIdentify".equals(propertyNames[i])){
                if(previousState[i]==null)
                    currentState[i]=user.getDep().getId();
            }
        }
    }

    /**
     * 删除时（改变删除标志位）,填充删除信息
     * @param propertyNames
     * @param currentState
     */
    private void doDel(String[] propertyNames,Object[] currentState, Object[] previousState){
        SessionUser user=getSessionUser();
        for(int i=0;i<propertyNames.length;i++){
            if("_delUserId".equals(propertyNames[i])){
                currentState[i]=user.getUserId();
            }
            if("_delUerName".equals(propertyNames[i])){
                currentState[i]=user.getRealName();
            }
            if("_delDate".equals(propertyNames[i])){
                currentState[i]=new Date();
            }
            if("depIdIdentify".equals(propertyNames[i])){
                if(previousState[i]==null)
                    currentState[i]=user.getDep().getId();
            }

        }
    }

}
