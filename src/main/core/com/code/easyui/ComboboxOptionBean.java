package com.code.easyui;

/**
 * @author zzl
 *         Date:2014-08-22
 */
public class ComboboxOptionBean {
    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ComboboxOptionBean() {
    }

    public ComboboxOptionBean(String value, String text) {
        this.value = value;
        this.text = text;
    }
}
