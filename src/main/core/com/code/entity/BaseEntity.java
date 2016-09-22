package com.code.entity;

import org.hibernate.annotations.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * 基本类
 * @author zzl
 * Date:2014-10-16
 */
@MappedSuperclass
@FilterDef(name = "depFilter",parameters = @ParamDef(name = "depId",type ="long"))
@Filter(name="depFilter",condition = "dep_id_identify in(:depId) ")
public class BaseEntity implements Serializable{
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="id",nullable=false,length=36)
    private String id;
    @Column(name="_create_user_id")
    private String _createUserId;
    @Column(name="_create_user_name")
    private String _createUserName;
    @Column(name="_create_date")
    private Date   _createDate;
    @Column(name="_update_user_id")
    private String _updateUserId;
    @Column(name="_update_user_name")
    private String _updateUserName;
    @Column(name="_update_date")
    private Date _updateDate;
    @Column(name="_del_user_id")
    private String _delUserId;
    @Column(name="_del_user_name")
    private String _delUerName;
    @Column(name="_del_date")
    private Date _delDate;
    @Column(name="_data_state")
    @Index(name = "_data_state_index")
    private Integer _dataState;
    @Column(name="dep_id_identify")
    @Index(name = "dep_id_identify_index")
    private Long depIdIdentify;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_createUserId() {
        return _createUserId;
    }

    public void set_createUserId(String _createUserId) {
        this._createUserId = _createUserId;
    }

    public String get_createUserName() {
        return _createUserName;
    }

    public void set_createUserName(String _createUserName) {
        this._createUserName = _createUserName;
    }

    public Date get_createDate() {
        return _createDate;
    }

    public void set_createDate(Date _createDate) {
        this._createDate = _createDate;
    }

    public String get_updateUserId() {
        return _updateUserId;
    }

    public void set_updateUserId(String _updateUserId) {
        this._updateUserId = _updateUserId;
    }

    public String get_updateUserName() {
        return _updateUserName;
    }

    public void set_updateUserName(String _updateUserName) {
        this._updateUserName = _updateUserName;
    }

    public Date get_updateDate() {
        return _updateDate;
    }

    public void set_updateDate(Date _updateDate) {
        this._updateDate = _updateDate;
    }

    public String get_delUserId() {
        return _delUserId;
    }

    public void set_delUserId(String _delUserId) {
        this._delUserId = _delUserId;
    }

    public String get_delUerName() {
        return _delUerName;
    }

    public void set_delUerName(String _delUerName) {
        this._delUerName = _delUerName;
    }

    public Date get_delDate() {
        return _delDate;
    }

    public void set_delDate(Date _delDate) {
        this._delDate = _delDate;
    }

    public BaseEntity(String id) {
        this.id=id;
    }

    public BaseEntity() {
    }

    public Integer get_dataState() {
        return _dataState;
    }

    public void set_dataState(Integer _dataState) {
        this._dataState = _dataState;
    }

    public Long getDepIdIdentify() {
        return depIdIdentify;
    }

    public void setDepIdIdentify(Long depIdIdentify) {
        this.depIdIdentify = depIdIdentify;
    }
}
