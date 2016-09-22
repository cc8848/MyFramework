package com.code.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import com.code.entity.BaseEntity;

@Entity
@Table(name = "code_template_setting")
public class CodeTemplateSettingEntity   extends BaseEntity{
    //生成类路径
    @Column(name="java_path" ,length = 200  ,nullable = true)
    private String  javaPath;
    //生成视图路径
    @Column(name="view_path" ,length = 200  ,nullable = true)
    private String  viewPath;

    public CodeTemplateSettingEntity(){

    }

    public CodeTemplateSettingEntity(String id){
        setId(id);
    }

    public String getJavaPath(){
        return javaPath;
    }
    public void setJavaPath(String javaPath){
        this.javaPath=javaPath;
    }
    public String getViewPath(){
        return viewPath;
    }
    public void setViewPath(String viewPath){
        this.viewPath=viewPath;
    }
}
