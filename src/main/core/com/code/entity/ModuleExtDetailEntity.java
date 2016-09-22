package com.code.entity;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

/**
 *扩展组件详情
 *2014-12-05
 **/
@Entity
@Table(name = "module_ext_detail")
public class ModuleExtDetailEntity   implements Serializable {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="id",nullable=false,length=36)
    private String id;
    //组件链接
    @Column(name="module_link" ,length = 200  ,nullable = true)
    private String  moduleLink;
    //组件类型
    @Column(name="module_type" ,length = 50  ,nullable = true)
    private String  moduleType;
    //module_ext_id
    @ManyToOne()
    @JoinColumn(name ="module_ext_id",referencedColumnName="id")
    @JsonIgnore
    private ModuleExtEntity moduleExtEntity;
    public ModuleExtDetailEntity(){

    }

    public ModuleExtDetailEntity(String id){
        setId(id);
    }
    public String getModuleLink(){
        return moduleLink;
    }
    public void setModuleLink(String moduleLink){
        this.moduleLink=moduleLink;
    }
    public String getModuleType(){
        return moduleType;
    }
    public void setModuleType(String moduleType){
        this.moduleType=moduleType;
    }
    public ModuleExtEntity getModuleExtEntity(){
        return moduleExtEntity;
    }
    public void setModuleExtEntity(ModuleExtEntity moduleExtEntity){
        this.moduleExtEntity=moduleExtEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
