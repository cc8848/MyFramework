package com.code.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 表头信息
 * @author zzl
 * Date:2014-08-01
 */
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "code_table_head")
public class TableHeadBean implements Serializable {
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="id",nullable=false,length=36)
    private String id;
    @Column(name="table_name",length = 50)
    private String tableName;
    @Column(name="table_content",length = 100)
    private String tableContent;
    @Column(name="sub_table_name",length = 100)
    private String subTableName;
    @Column(name="table_view_type",length = 20)
    private String tableViewType;
    @Column(name="dh_field",length = 20)
    private String dhField;
    @Column(name="dh_field_rule",length = 200)
    private String dhFieldRule;

    @Column(name="dynamic_entity",length = 3)
    private Integer dyEntity=0;

    @Column(name="dicflh",length = 200)
    private String dicflh;

    /*@OneToMany(mappedBy = "head",fetch = FetchType.LAZY)
    @Cascade(CascadeType.DELETE)
    @org.hibernate.annotations.OrderBy(clause="fieldOrder asc")*/
    @Transient
    @JsonIgnore
    private List<TableFieldBean> fields;
    @Transient
    private String javaBasePath;
    @Transient
    private String viewBasePath;
    @Column(name="base_package_name",length = 200)
    private String basePackageName;
    @Column(name="view_folder",length = 200)
    private String viewFolder;
    @Transient
    private List<TableHeadBean> subList;
    @Transient
    private String showViewStyle;
    @Column(name="is_index",length = 3)
    private Integer isIndex=0;


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

    public String getTableViewType() {
        return tableViewType;
    }

    public void setTableViewType(String tableViewType) {
        this.tableViewType = tableViewType;
    }

    public List<TableFieldBean> getFields() {
        return fields;
    }

    public void setFields(List<TableFieldBean> fields) {
        this.fields = fields;
    }

    public String getJavaBasePath() {
        return javaBasePath;
    }

    public void setJavaBasePath(String javaBasePath) {
        this.javaBasePath = javaBasePath;
    }

    public String getViewBasePath() {
        return viewBasePath;
    }

    public void setViewBasePath(String viewBasePath) {
        this.viewBasePath = viewBasePath;
    }
    public String getBasePackageName() {
        return basePackageName;
    }

    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }
    public List<TableHeadBean> getSubList() {
        return subList;
    }

    public void setSubList(List<TableHeadBean> subList) {
        this.subList = subList;
    }
    public String getViewFolder() {
        return viewFolder;
    }

    public void setViewFolder(String viewFolder) {
        this.viewFolder = viewFolder;
    }

    public TableHeadBean(String id) {
        this.id = id;
    }

    public TableHeadBean() {
    }
    public String getShowViewStyle() {
        return showViewStyle;
    }

    public void setShowViewStyle(String showViewStyle) {
        this.showViewStyle = showViewStyle;
    }

    public Integer getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(Integer isIndex) {
        this.isIndex = isIndex;
    }

    public String getDhField() {
        return dhField;
    }

    public void setDhField(String dhField) {
        this.dhField = dhField;
    }

    public String getDhFieldRule() {
        return dhFieldRule;
    }

    public void setDhFieldRule(String dhFieldRule) {
        this.dhFieldRule = dhFieldRule;
    }

    public Integer getDyEntity() {
        return dyEntity;
    }

    public void setDyEntity(Integer dyEntity) {
        this.dyEntity = dyEntity;
    }

    public boolean isDynamicEntity(){
        return Objects.equals(1,dyEntity);
    }

    public String getDicflh() {
        return dicflh;
    }

    public void setDicflh(String dicflh) {
        this.dicflh = dicflh;
    }
}
