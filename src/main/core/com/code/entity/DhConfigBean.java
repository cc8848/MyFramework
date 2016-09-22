package com.code.entity;

/**
 * Created by lenovo on 2014/12/12.
 */
public class DhConfigBean {

    private String dhField;
    private String dhFieldRule;
    private TableFieldBean tableFieldBean;
    private String tableName;
    private String parentDh="";

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

    public TableFieldBean getTableFieldBean() {
        return tableFieldBean;
    }

    public void setTableFieldBean(TableFieldBean tableFieldBean) {
        this.tableFieldBean = tableFieldBean;
    }

    public DhConfigBean() {
    }

    public DhConfigBean(String dhField, String dhFieldRule, TableFieldBean tableFieldBean) {
        this.dhField = dhField;
        this.dhFieldRule = dhFieldRule;
        this.tableFieldBean = tableFieldBean;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getParentDh() {
        return parentDh;
    }

    public void setParentDh(String parentDh) {
        this.parentDh = parentDh;
    }
}
