package com.sys.entity;

import java.util.List;

/**
 * @author zzl
 * Date:2014-08-27
 */
public class RoleTreeBean extends TreeBean {
    private boolean preThrough;
    public boolean isPreThrough() {
        return preThrough;
    }

    public void setPreThrough(boolean preThrough) {
        this.preThrough = preThrough;
    }

    public RoleTreeBean(Long id, String text, boolean preThrough) {
        super(id, text, "");
        this.preThrough = preThrough;
    }

    public RoleTreeBean() {
    }

    public RoleTreeBean(Long id, String text,  boolean preThrough, boolean checked) {
        super(id, text,null, checked);
        this.preThrough = preThrough;
    }
}
