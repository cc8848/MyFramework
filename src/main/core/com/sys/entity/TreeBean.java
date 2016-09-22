package com.sys.entity;

import java.util.List;

/**
 * 树
 * 可用于easyui tree 和combotree
 * @author zzl
 * Date:2014-08-25
 */
public class TreeBean {
    private Long id;
    private String text;
    private String url;
    private List<TreeBean> children;
    private boolean checked;
    public TreeBean() {
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

    public List<TreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<TreeBean> children) {
        this.children = children;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TreeBean(Long id, String text, String url) {
        this.id = id;
        this.text = text;
        this.url = url;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public TreeBean(Long id, String text, String url, boolean checked) {
        this.id = id;
        this.text = text;
        this.url = url;
        this.checked = checked;
    }
}
