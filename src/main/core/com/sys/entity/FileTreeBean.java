package com.sys.entity;

import java.util.List;

public class FileTreeBean {
    private String name;
    private String path;
    private List<FileTreeBean> children;
    private String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FileTreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<FileTreeBean> children) {
        this.children = children;
    }

    public FileTreeBean(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public FileTreeBean() {
    }

    @Override
    public String toString() {
        return path;
    }
    public String getId(){
        return path;
    }
    public String getText(){
        return name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
