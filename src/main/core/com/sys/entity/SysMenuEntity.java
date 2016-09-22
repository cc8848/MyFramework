package com.sys.entity;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "sys_menu")
public class SysMenuEntity  implements Serializable{
    //id
    @Id
    @GenericGenerator(name = "generator", strategy = "assigned")
    @Column(name ="ID",nullable=false,length=15)
    private Long  id;
    //pid
    @Column(name="pid" ,length = 15  ,nullable = true)
    private Long  pid;
    //菜单名称
    @Column(name="text" ,length = 50  ,nullable = true)
    private String  text;
    //图标
    @Column(name="pic" ,length = 100  ,nullable = true)
    private String  pic;
    //链接
    @Column(name="url" ,length = 200  ,nullable = true)
    private String menuLink;
    //排序号
    @Column(name="xh" ,length = 3  ,nullable = true)
    private Integer  xh;
    //响应id
    @Column(name="resid" ,length = 20  ,nullable = true)
    private String  resid;
    //菜单类型
    @Column(name="lx" ,length = 20  ,nullable = true)
    private String  lx;
    @Transient
    private List<SysMenuEntity> children;
    @Transient
    private List<MenuButtonEntity> buttonEntities;
    @Transient
    private boolean checked;

    public SysMenuEntity(){

    }

    public SysMenuEntity(Long id){
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
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text=text;
    }
    public String getPic(){
        return pic;
    }
    public void setPic(String pic){
        this.pic=pic;
    }
    public String getMenuLink(){
        return menuLink;
    }
    public void setMenuLink(String menuLink){
        this.menuLink = menuLink;
    }
    public Integer getXh(){
        return xh;
    }
    public void setXh(Integer xh){
        this.xh=xh;
    }
    public String getResid(){
        return resid;
    }
    public void setResid(String resid){
        this.resid=resid;
    }
    public String getLx(){
        return lx;
    }
    public void setLx(String lx){
        this.lx=lx;
    }

    public List<SysMenuEntity> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuEntity> children) {
        this.children = children;
    }

    public List<MenuButtonEntity> getButtonEntities() {
        return buttonEntities;
    }

    public void setButtonEntities(List<MenuButtonEntity> buttonEntities) {
        this.buttonEntities = buttonEntities;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    @Transient
    public String getName(){
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysMenuEntity that = (SysMenuEntity) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }




}
