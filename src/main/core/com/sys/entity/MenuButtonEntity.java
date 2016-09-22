package com.sys.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by lenovo on 2014/8/27.
 */
@Entity
@Table(name = "sys_menu_button")
public class MenuButtonEntity {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=36)
    private String id;
    @Column(name="menu_id" ,length = 15  ,nullable = false)
    private Long menuId;
    @Column(name="button_name" ,length = 50  ,nullable = false)
    private String buttonName;
    @Column(name="button_code" ,length = 50  ,nullable = false)
    private String buttonCode;
    @Column(name="method_name" ,length = 50  ,nullable = false)
    private String methodName;
    @Transient
    private boolean hasPermission;
    public MenuButtonEntity() {
    }
    public MenuButtonEntity(String id) {
        this.id=id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonCode() {
        return buttonCode;
    }

    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }
}
