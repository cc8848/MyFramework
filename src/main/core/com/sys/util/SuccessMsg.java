package com.sys.util;

import java.util.HashMap;
import java.util.Map;

/**
 * ajax提交后返回信息
 * @author zzl
 * Date:2014-07-28
 */
public class SuccessMsg {
    //是否成功
    private boolean isSuccess;
    //信息
    private String msg;
    private Map<String,Object> dataMap=new HashMap<>(0);
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SuccessMsg(boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.msg = msg;
    }

    public SuccessMsg() {
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }
}
