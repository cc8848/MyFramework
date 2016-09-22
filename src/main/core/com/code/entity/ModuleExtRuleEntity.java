package com.code.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
*扩展组件与表对应规则
*2014-12-05
**/
@Entity
@Table(name = "module_ext_rule")
public class ModuleExtRuleEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="id",nullable=false,length=36)
    private String id;
    //组件id
    @Column(name="module_id" ,length = 40  ,nullable = true)
    private String  moduleId;
    //对应表名
    @Column(name="module_table" ,length = 500  ,nullable = true)
    private String  moduleTable;

    public ModuleExtRuleEntity(){

    }

    public ModuleExtRuleEntity(String id){
        setId(id);
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleTable() {
        return moduleTable;
    }

    public void setModuleTable(String moduleTable) {
        this.moduleTable = moduleTable;
    }

    public ModuleExtRuleEntity(String id, String moduleId, String moduleTable) {
        setId(id);
        this.moduleId = moduleId;
        this.moduleTable = moduleTable;
    }

    public ModuleExtRuleEntity(String moduleId, String moduleTable) {
        this.moduleId = moduleId;
        this.moduleTable = moduleTable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
