package com.sys.util;

/**
 * @author zzl
 *         Date:2014-07-28
 */
public class ExceptionUtil {

    public static String generateExceptionMessage(Class clazz,String method){
        return clazz.getName()+"中的"+method+"方法执行失败!";
    }
}
