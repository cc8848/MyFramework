package com.sys.entity;

import com.code.entity.BaseTreeEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2014/12/21.
 */
public class ComboTree extends BaseTreeEntity{
    private String cKey;

    public String getcKey() {
        return cKey;
    }

    public void setcKey(String cKey) {
        this.cKey = cKey;
    }

    public ComboTree(String cKey) {
        this.cKey = cKey;
    }

    public ComboTree() {
    }
    public String getName(){
        return getText();
    }
    public ComboTree(String id,String pid,String cKey,String text){
        setId(id);
        setpId(pid);
        setcKey(cKey);
        setText(text);
    }
    public ComboTree(BaseTreeEntity entity){
        setId(entity.getId());
        setpId(entity.getpId());
        setText(entity.getText());
        setChildren(entity.getChildren());
    }


}
