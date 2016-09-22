package com.code.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
*扩展组件管理
*2014-12-05
**/
@Entity
@Table(name = "module_ext")
public class ModuleExtEntity  implements Serializable {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="id",nullable=false,length=36)
    private String id;
    //组件名称
    @Column(name="module_name" ,length = 200  ,nullable = true)
    private String  moduleName;
    //组件代号
    @Column(name="module_code" ,length = 100  ,nullable = true)
    private String  moduleCode;
    //默认显示
    @Column(name="default_show" ,length = 3  ,nullable = true)
    private Integer  defaultShow;
    @Transient
    @JsonIgnore
    private List<ModuleExtDetailEntity> moduleExtDetails;
    public ModuleExtEntity(){

    }
    @Transient
    private boolean checked;
    public ModuleExtEntity(String id){
        setId(id);
    }
    public String getModuleName(){
        return moduleName;
    }
    public void setModuleName(String moduleName){
        this.moduleName=moduleName;
    }
    public String getModuleCode(){
        return moduleCode;
    }
    public void setModuleCode(String moduleCode){
        this.moduleCode=moduleCode;
    }
    public Integer getDefaultShow(){
        return defaultShow;
    }
    public void setDefaultShow(Integer defaultShow){
        this.defaultShow=defaultShow;
    }
    public  List<ModuleExtDetailEntity> getModuleExtDetails(){
        return moduleExtDetails;
    }
    public void setModuleExtDetails(List<ModuleExtDetailEntity> moduleExtDetails){
       this.moduleExtDetails=moduleExtDetails;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Transient
    public boolean isShow(){
        return defaultShow==1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
