package com.code.entity;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 基本类
 * @author zzl
 * Date:2014-10-16
 */
@MappedSuperclass
public class BaseTreeEntity implements Serializable{
    @Expose
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="id",nullable=false,length=36)
    private String id;
    @Expose
    @Column(name ="pid",length=40)
    private String pId="0";
    @Transient
    private List<? extends BaseTreeEntity> children;
    @Transient
    private String text;
    @Expose
    @Column(name="_create_date")
    private Date   _createDate;

    public Date get_createDate() {
        return _createDate;
    }

    public void set_createDate(Date _createDate) {
        this._createDate = _createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getPId() {
        if(pId==null||pId.trim().length()==0)
            pId="0";
        return pId;
    }

    public void setPId(String pId) {
        if(pId==null||pId.trim().length()==0)
            pId="0";
        this.pId = pId;
    }
    public String getpId() {
        if(pId==null||pId.trim().length()==0)
            pId="0";
        return pId;
    }

    public void setpId(String pId) {
        if(pId==null||pId.trim().length()==0)
            pId="0";
        this.pId = pId;
    }
    public List<? extends BaseTreeEntity> getChildren() {
        return children;
    }

    public void setChildren(List<? extends BaseTreeEntity> children) {
        this.children = children;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
