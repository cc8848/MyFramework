package com.sys.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "sys_role")
public class SysRoleEntity  implements Serializable{
    //id
    @Id
    @GenericGenerator(name = "generator", strategy = "assigned")
    @Column(name ="ID",nullable=false,length=15)
    private Long  id;
    //pid
    @Column(name="pid" ,length = 15  ,nullable = true)
    private Long  pid;
    //角色编号
    @Column(name="role_code" ,length = 20  ,nullable = true)
    private String  roleCode;
    //角色名称
    @Column(name="role_name" ,length = 50  ,nullable = true)
    private String  roleName;
    //角色描述
    @Column(name="role_desc" ,length = 50  ,nullable = true)
    private String  roleDesc;
    //允许上级拥有本角色权限
    @Column(name="pre_through" ,length = 2  ,nullable = true)
    private Integer  preThrough;
    @Column(name="xh" ,length = 2  ,nullable = true)
    private Integer xh;

    public SysRoleEntity(){

    }

    public SysRoleEntity(Long id){
    this.id=id;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public Long getPid(){
        return pid;
    }
    public void setPid(Long pid){
        this.pid=pid;
    }
    public String getRoleCode(){
        return roleCode;
    }
    public void setRoleCode(String roleCode){
        this.roleCode=roleCode;
    }
    public String getRoleName(){
        return roleName;
    }
    public void setRoleName(String roleName){
        this.roleName=roleName;
    }
    public String getRoleDesc(){
        return roleDesc;
    }
    public void setRoleDesc(String roleDesc){
        this.roleDesc=roleDesc;
    }
    public Integer getPreThrough(){
        return preThrough;
    }
    public void setPreThrough(Integer preThrough){
        this.preThrough=preThrough;
    }

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }
    @Transient
    @JsonIgnore
    public boolean isPreThrough(){
        return Objects.equals(1,preThrough);
    }
}
