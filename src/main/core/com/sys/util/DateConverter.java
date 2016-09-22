package com.sys.util;

import org.apache.commons.beanutils.Converter;

import java.util.Date;

/**
 * Created by lenovo on 2014/7/28.
 */
public class DateConverter implements Converter {
    private  String formatStr;
    public DateConverter() {
        formatStr=DateUtil.YYYYMMDD;
    }
    public DateConverter(String formatStr){
        this.formatStr=formatStr;
    }
    @Override
    public Object convert(Class aClass, Object o) {
        if(o==null)
            return null;
        if(o instanceof String)
             return DateUtil.parse((String) o,formatStr);
        else if(o instanceof Long)
            return new Date((Long)o);
        else
            return o;

    }
}
