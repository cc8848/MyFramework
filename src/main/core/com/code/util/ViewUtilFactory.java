package com.code.util;

import com.code.easyui.EasyuiViewUtil;

/**
 * @author zzl
 *         Date:2014-08-09
 */
public class ViewUtilFactory {
    public static ViewUtil getInstance(String type){
        return new EasyuiViewUtil();
    }
}
