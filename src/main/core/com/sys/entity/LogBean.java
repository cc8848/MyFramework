package com.sys.entity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zzl
 *         Date:2014-07-30
 */
@Entity
@Table(name = "sys_log")
public class LogBean implements Serializable {
    private String id;
    private String msg;
    private String oType;
    private String userName;
    private String ipAddress;
    private String browerType;
    private Date createDate;
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(StringUtils.isNotBlank(id))
        this.id = id;
    }
    @Column(name="log_msg")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    @Column(name="log_type")
    public String getoType() {
        return oType;
    }

    public void setoType(String oType) {
        this.oType = oType;
    }
    @Column(name="log_username")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Column(name="log_ip")
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    @Column(name="log_brower")
    public String getBrowerType() {
        return browerType;
    }

    public void setBrowerType(String browerType) {
        this.browerType = browerType;
    }
    @Column(name="create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
