package com.basetemplate.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 模板头信息
 * @author zzl
 * Date:2014-08-22
 */
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "template_table_head")
public class TemplateHeadBean implements Serializable {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 36)
    private String id;
    @Column(name = "table_name", length = 50)
    private String tableName;
    @Column(name = "table_content", length = 100)
    private String tableContent;
    @Column(name = "sub_table_name", length = 100)
    private String subTableName;
    @Transient
    @JsonIgnore
    private List<TemplateFieldBean> fields;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableContent() {
        return tableContent;
    }

    public void setTableContent(String tableContent) {
        this.tableContent = tableContent;
    }

    public String getSubTableName() {
        return subTableName;
    }

    public void setSubTableName(String subTableName) {
        this.subTableName = subTableName;
    }

    public List<TemplateFieldBean> getFields() {
        return fields;
    }

    public void setFields(List<TemplateFieldBean> fields) {
        this.fields = fields;
    }

}
