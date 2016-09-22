package com.sys.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author zzl
 * Date:2014-08-28
 */
@Entity
@Table(name = "sys_role_user")
public class RoleUserEntity {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=36)
    private String id;
    @Column(name="user_id" ,length = 36  ,nullable = true)
    private String userId;
    @Column(name="role_id" ,length = 15  ,nullable = true)
    private Long roleId;
    @Column(name="default_role" ,length = 2  ,nullable = true)
    private int  defaultRole;

    public RoleUserEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public int getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(int defaultRole) {
        this.defaultRole = defaultRole;
    }
    @Transient
    @JsonIgnore
    public boolean isDefault(){
        return Objects.equals(1,defaultRole);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
