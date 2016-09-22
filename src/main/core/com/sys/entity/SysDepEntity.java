package com.sys.entity;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sys_dep")
public class SysDepEntity implements Serializable{
    //id
    @Id
    @GenericGenerator(name = "generator", strategy = "assigned")
    @Column(name ="ID",nullable=false)
    private Long  id;

    public String getDepLogoId() {
        return depLogoId;
    }

    public void setDepLogoId(String depLogoId) {
        this.depLogoId = depLogoId;
    }

    //部门标识名称
    @Column(name="dep_logo_id" ,length = 100  ,nullable = true)
    private String  depLogoId;

    //部门名称
    @Column(name="dep_name" ,length = 100  ,nullable = true)
    private String  depName;
    //部门描述
    @Column(name="dep_desc" ,length = 200  ,nullable = true)
    private String  depDesc;
    //pid
    @Column(name="pid" ,length = 36  ,nullable = true)
    private Long  pid;
    private Integer xh;
    public SysDepEntity(){

    }

    public SysDepEntity(Long id){
    this.id=id;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getDepName(){
        return depName;
    }
    public void setDepName(String depName){
        this.depName=depName;
    }
    public String getDepDesc(){
        return depDesc;
    }
    public void setDepDesc(String depDesc){
        this.depDesc=depDesc;
    }
    public Long getPid(){
        return pid;
    }
    public void setPid(Long pid){
        this.pid=pid;
    }

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }

}
