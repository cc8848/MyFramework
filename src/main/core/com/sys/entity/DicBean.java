package com.sys.entity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zzl
 *         Date:2014-07-28
 */
@Entity
@Table(name = "sys_dic")
public class DicBean implements Serializable {
    private String id;
    private String dicKey;
    private String dicValue;
    private String dicType;
    private String dicTypeDesc;
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
    @Column(name="dic_key",length = 50,nullable = true)
    public String getDicKey() {
        return dicKey;
    }

    public void setDicKey(String dicKey) {
        this.dicKey = dicKey;
    }
    @Column(name="dic_value",length = 100)
    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }
    @Column(name = "dic_type",length = 50)
    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }
    @Column(name = "dic_type_desc",length = 100)
    public String getDicTypeDesc() {
        return dicTypeDesc;
    }

    public void setDicTypeDesc(String dicTypeDesc) {
        this.dicTypeDesc = dicTypeDesc;
    }

    public DicBean(String id) {
        this.id = id;
    }

    public DicBean() {
    }
}
