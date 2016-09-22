package com.basetemplate.entity;


import com.code.entity.TableHeadBean;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 模板字段信息
 * @author zzl
 * Date:2014-08-01
 */
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "template_table_field")
public class TemplateFieldBean implements Serializable{
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=36)
    private String id;
    @Column(name="field_name",length = 50)
    private String fieldName;
    @Column(name="field_content",length = 100)
    private String fieldContent;
    @Column(name="field_type",length = 20)
    private String fieldType;
    @Column(name="field_length")
    private Float  fieldLength;
    @Column(name="dic_key",length = 50)
    private String dicKey;
    @Column(name="dic_value",length = 100)
    private String dicValue;
    @Column(name="dic_table",length = 50)
    private String dicTable;
    @Column(name="field_valid_type",length = 50)
    private String fieldValidType;
    @Column(name="is_key",length = 2)
    private Integer    isKey;
    @Column(name="is_null",length = 2)
    private Integer    isNull;
    @Column(name="is_query",length = 2)
    private Integer    isQuery;
    @Column(name="is_insert",length = 2)
    private Integer    isInsert;
    @Column(name="is_show",length = 2)
    private Integer    isShow;
    @Column(name="is_show_list",length = 2)
    private Integer    isShowList;
    @Column(name="show_length")
    private Float  showLength;
    @Column(name="main_id",length = 36)
    private String mainId;
    @Column(name="main_table",length = 50)
    private String mainTable;
    @Column(name="template_name",length = 100)
    private String templateName;
    @Column(name="query_model",length = 20)
    private String queryModel;
    @Column(name="show_type",length = 50)
    private String showType;
    @Column(name="field_order",length = 3)
    private Integer    fieldOrder;
    @Column(name="field_default",length = 150)
    private String fieldDefault;
    @Column(name="field_version")
    private Float  fieldVersion;
    @ManyToOne()
    @JoinColumn(name ="template_head_id",referencedColumnName="id")
    @JsonIgnore
    private TemplateHeadBean head;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldContent() {
        return fieldContent;
    }

    public void setFieldContent(String fieldContent) {
        this.fieldContent = fieldContent;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Float getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Float fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getDicKey() {
        if(dicKey==null) dicKey="";
        return dicKey;
    }

    public void setDicKey(String dicKey) {
        this.dicKey = dicKey;
    }

    public String getDicValue() {
        if(dicValue==null) dicValue="";
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public String getDicTable() {
        if(dicTable==null) dicTable="";
        return dicTable;
    }

    public void setDicTable(String dicTable) {
        this.dicTable = dicTable;
    }

    public String getFieldValidType() {
        return fieldValidType;
    }

    public void setFieldValidType(String fieldValidType) {
        this.fieldValidType = fieldValidType;
    }

    public Integer getIsKey() {
        return isKey;
    }

    public void setIsKey(Integer isKey) {
        this.isKey = isKey;
    }

    public Integer getIsNull() {
        return isNull;
    }

    public void setIsNull(Integer isNull) {
        this.isNull = isNull;
    }

    public Integer getIsQuery() {
        return isQuery;
    }

    public void setIsQuery(Integer isQuery) {
        this.isQuery = isQuery;
    }

    public Integer getIsInsert() {
        return isInsert;
    }

    public void setIsInsert(Integer isInsert) {
        this.isInsert = isInsert;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsShowList() {
        return isShowList;
    }

    public void setIsShowList(Integer isShowList) {
        this.isShowList = isShowList;
    }

    public Float getShowLength() {
        return showLength;
    }

    public void setShowLength(Float showLength) {
        this.showLength = showLength;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getMainTable() {
        return mainTable;
    }

    public void setMainTable(String mainTable) {
        this.mainTable = mainTable;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getQueryModel() {
        return queryModel;
    }

    public void setQueryModel(String queryModel) {
        this.queryModel = queryModel;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public Integer getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public String getFieldDefault() {
        return fieldDefault;
    }

    public void setFieldDefault(String fieldDefault) {
        this.fieldDefault = fieldDefault;
    }

    public Float getFieldVersion() {
        return fieldVersion;
    }

    public void setFieldVersion(Float fieldVersion) {
        this.fieldVersion = fieldVersion;
    }

    public TemplateHeadBean getHead() {
        return head;
    }

    public void setHead(TemplateHeadBean head) {
        this.head = head;
    }
    @Transient
    public boolean isNull(){
        return isNull==null?false:isNull==1;
    }
    @Transient
    public boolean isKey(){
        return isKey==null?false:isKey==1;
    }
    @Transient
    public int getLength(){
        if(fieldLength==null)
            fieldLength=0f;
        return fieldLength.intValue();
    }
    @Transient
    public boolean isQuery(){
        return Objects.equals(1,isQuery);
    }
    @Transient
    public boolean isShow(){
        return Objects.equals(1,isShow);
    }
    @Transient
    public boolean isInsert(){
        return Objects.equals(1,isInsert);
    }
    @Transient
    public boolean isShowList(){
        return Objects.equals(1,isShowList);
    }
    @Transient
    public int getPrecision(){
        if(fieldLength==null)
            return 0;
        else{
            return Math.abs((int)(fieldLength*10-getLength()*10));
        }
    }
    @Transient
    public int getTextLength(){
        if(showLength==null)
            return 0;
        else
            return showLength.intValue();
    }
    @Transient
    public int getTextPrecision(){
        if(showLength==null)
            return 0;
        else
            return Math.abs((int)(showLength*10-getTextLength()*10));
    }

    public TemplateFieldBean() {
    }

    public TemplateFieldBean(String id) {
        this.id = id;
    }
    @Transient
    public boolean hasMain(){
        return StringUtils.isNotBlank(mainId)&&StringUtils.isNotBlank(mainTable);
    }


}
