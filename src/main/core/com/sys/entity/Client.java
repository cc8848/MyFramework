package com.sys.entity;

import com.sys.filter.DataFilter;

import java.util.*;

/**
 * 在线用户信息
 * Created by lenovo on 2014/8/27.
 */
public class Client {
    private SysUserEntity userEntity;
    private List<SysRoleEntity> roleEntities;
    private Map<String,SysMenuEntity> menuEntityMap;
    private Set<DataFilter> dataFilters=new LinkedHashSet<>(0);
    private String ipAddress;
    private Date loginTime;
    private Set<Long> menuIdSet;

    public SysUserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(SysUserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public List<SysRoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(List<SysRoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public Map<String, SysMenuEntity> getMenuEntityMap() {
        return menuEntityMap;
    }

    public void setMenuEntityMap(Map<String, SysMenuEntity> menuEntityMap) {
        this.menuEntityMap = menuEntityMap;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Client() {
    }

    public Client(SysUserEntity userEntity, List<SysRoleEntity> roleEntities, Map<String, SysMenuEntity> menuEntityMap, String ipAddress, Date loginTime) {
        this.userEntity = userEntity;
        this.roleEntities = roleEntities;
        this.menuEntityMap = menuEntityMap;
        this.ipAddress = ipAddress;
        this.loginTime = loginTime;
    }

    public Set<DataFilter> getDataFilters() {
        return dataFilters;
    }

    public void setDataFilters(Set<DataFilter> dataFilters) {
        this.dataFilters = dataFilters;
    }

    public void addDataFilter(DataFilter filter){
        if(dataFilters==null)
            dataFilters=new LinkedHashSet<>();
        dataFilters.add(filter);
    }

    public Set<Long> getMenuIdSet() {
        return menuIdSet;
    }

    public void setMenuIdSet(Set<Long> menuIdSet) {
        this.menuIdSet = menuIdSet;
    }
}
