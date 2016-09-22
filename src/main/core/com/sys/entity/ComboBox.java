package com.sys.entity;

import java.io.Serializable;

/**
 * @author zzl
 *         Date:2014-08-05
 */
public class ComboBox implements Serializable{
    private Object cKey;
    private Object cValue;

    public ComboBox() {
    }

    public ComboBox(Object cKey, Object cValue) {
        this.cKey = cKey;
        this.cValue = cValue;
    }

    public Object getcKey() {
        return cKey;
    }

    public void setcKey(Object cKey) {
        this.cKey = cKey;
    }

    public Object getcValue() {
        return cValue;
    }

    public void setcValue(Object cValue) {
        this.cValue = cValue;
    }
    public String getValueKey(){
        return cValue+"["+cKey+"]";
    }
}
