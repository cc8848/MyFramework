package com.sys.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zzl
 * Date:2014-08-29
 */
@Entity
@Table(name = "sys_role_resource")
public class RoleMenuEntity implements Serializable{
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=36)
    private String id;
    @Column(name ="role_id",nullable=false,length=15)
    private Long roleId;
    @Column(name ="menu_id",nullable=false,length=15)
    private Long menuId;
    @Column(name ="button_code",nullable=true,length=300)
    private String buttonCode;

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

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getButtonCode() {
        return buttonCode;
    }

    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }

}
