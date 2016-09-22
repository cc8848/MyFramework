package com.sys.entity;

import java.util.List;
import java.util.Map;

/**
 * @author zzl
 *         Date:2014-08-29
 */
public class RoleResourceTreeBean  {
    private Long id;
    private String text;
    private String url;
    private List<RoleResourceTreeBean> children;
    private boolean checked;
    private Map<String,Object> attributes;

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<RoleResourceTreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<RoleResourceTreeBean> children) {
        this.children = children;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public RoleResourceTreeBean() {
    }

    public RoleResourceTreeBean(Long id, String text, String url, boolean checked, Map<String,Object> attributes) {
        this.id = id;
        this.text = text;
        this.url = url;
        this.checked = checked;
        this.attributes = attributes;
    }

}
