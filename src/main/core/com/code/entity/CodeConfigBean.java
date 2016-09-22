package com.code.entity;

import java.util.List;

/**
 * Created by lenovo on 14-8-13.
 */
public class CodeConfigBean {
    private String basePackageName;
    private String viewFolder;
    private List<String> codeType;
    private String headId;
    private String code_t;
    public String getBasePackageName() {
        return basePackageName;
    }

    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }

    public String getViewFolder() {
        return viewFolder;
    }

    public void setViewFolder(String viewFolder) {
        this.viewFolder = viewFolder;
    }

    public List<String> getCodeType() {
        return codeType;
    }

    public void setCodeType(List<String> codeType) {
        this.codeType = codeType;
    }

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }

    public String getCode_t() {
        return code_t;
    }

    public void setCode_t(String code_t) {
        this.code_t = code_t;
    }
}
