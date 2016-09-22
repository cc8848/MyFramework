/***********************************************************************
 * Module:  StringUtil.java
 * Author:  lenovo
 * Purpose: Defines the Class StringUtil
 ***********************************************************************/

package com.sys.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.regex.Pattern;

/** 字符串工具类
 * 
 * @pdOid c5582f6b-f25f-4a19-989f-e0bb668a2781 */
public class StringUtil {
   /** 判断字符串是否能转换成数字
    * 
    * @param str
    * @pdOid 643dac2c-b3df-4104-b898-b399fab1b113 */
   public static boolean isNumeric(String str) {
       Pattern pattern = Pattern.compile("[0-9.]*");
       return pattern.matcher(str).matches();
   }
   
   /** 转成float
    * 
    * @param str
    * @pdOid a6ed0798-300f-4e94-b6b0-e554db535db3 */
   public static float toFloat(String str) {
        float f=0l;
        if(isNumeric(str)){
            f=Float.parseFloat(str);
        }
      return f;
   }
   
   /** 转换成int
    * 
    * @param str
    * @pdOid d99ffb8b-dc43-41be-8c03-563590e21ba1 */
   public static int toInt(String str) {
       int in=0;
       if(isNumeric(str)){
          in=Integer.parseInt(str);
       }
       return in;
   }
   
   /** 转换成long
    * 
    * @param str
    * @pdOid 78f39aef-83dd-49cb-9af7-227bc23fba22 */
   public static long toLong(String str) {
       long lo=0l;
       if(isNumeric(str)){
           lo=Long.parseLong(str);
       }
      return lo;
   }
    public static void main(String[]args){
        System.out.println(toGetMethodName("sys_dic"));

    }
    /**
     * 通过包名获取相对路径名
     * @param basePath
     * @param packageName
     * @return
     */
    public static String getPathByPackageName(String basePath,String packageName){
        if(packageName==null)
            return basePath;
        else{
            if(basePath.trim().length()>0&&!basePath.endsWith("/"))
                basePath+="/";
            return basePath+packageName.replace(".","/");
        }

    }

    /**
     * 数据库字段转java字段
     * @param voName
     * @return
     */
    public static String toFieldName(String voName) {
        StringBuffer sb = new StringBuffer();
        if(voName.startsWith("_"))
            sb.append("_");
        boolean flag=false;
        for (int i = 0; i < voName.length(); i++) {
            char cur = voName.charAt(i);
            if (cur=='_'&&i>0) {
                flag=true;
            } else {
                if(flag){
                    sb.append(Character.toUpperCase(cur));
                    flag=false;
                }else{
                    sb.append(Character.toLowerCase(cur));

                }

            }
        }
        return sb.toString();
    }

    /**
     * java字段转数据库字段
     * @param voName
     * @return
     */
    public static String toDataBaseName(String voName) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < voName.length(); i++) {
            char cur = voName.charAt(i);
            if (Character.isUpperCase(cur)) {
                sb.append("_");
                sb.append(cur);
            } else {
                sb.append(cur);
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 获取get方法名称
     * @param fieldName
     * @return
     */
    public static String toGetMethodName(String fieldName){
        String field=toFieldName(fieldName);
        return "get"+field.substring(0,1).toUpperCase()+field.substring(1,field.length());
    }
    public static String toGetMethodNameWithColName(String colName){
        if(colName.startsWith("_"))
            return "get"+colName;
        else
            return "get"+colName.substring(0,1).toUpperCase()+colName.substring(1,colName.length());
    }
    /**
     * 获取set方法名称
     * @param fieldName
     * @return
     */
    public static String toSetMethodName(String fieldName){
        String field=toFieldName(fieldName);
        return "set"+field.substring(0,1).toUpperCase()+field.substring(1,field.length());
    }

    /**
     * 数据库表名转java名
     * @param voName
     * @return
     */
    public static String toEntityName(String voName) {
        StringBuffer sb = new StringBuffer();
        boolean flag=false;
        for (int i = 0; i < voName.length(); i++) {
            char cur = voName.charAt(i);
            if (cur=='_') {
                flag=true;

            } else {
                if(i==0){
                    sb.append(Character.toUpperCase(cur));
                }else if(flag){
                    sb.append(Character.toUpperCase(cur));
                    flag=false;
                }else{
                    sb.append(Character.toLowerCase(cur));

                }

            }
        }
        return sb.toString();
    }

    /**
     * 根据request的请求方式（get,set）对字符串解码
     * @param str
     * @param method request.getMethod()
     * @return
     */
    public static String getDecodeStr(String str,String method){
        String val=str;
        if("GET".equals(method)){
            try{
                val=new String(str.getBytes("iso-8859-1"),"utf-8");
            }catch (Exception e){
                return str;
            }

        }
        return val;
    }

    /**
     * 根据类名获取类
     * @param className
     * @return
     */
    public static   Class getClassByClassName(String className){
        Class clazz=null;
        if(StringUtils.isNotBlank(className)){
            try {
                clazz=Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }
}