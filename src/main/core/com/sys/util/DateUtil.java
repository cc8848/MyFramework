/***********************************************************************
 * Module:  DateUtil.java
 * Author:  lenovo
 * Purpose: Defines the Class DateUtil
 ***********************************************************************/

package com.sys.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 日期工具类
 * 
 * @pdOid a8d1b869-b490-4a77-945a-8c084f5c5716 */
public class DateUtil {
   /** @pdOid ca4e1a60-d17b-4d7b-9641-442a6eb7fd9b */
   public static String YYYYMMDD = "yyyy-MM-dd";
    public static String YYMMDD = "yy-M月-dd";
   public static String TIMEFORFILE = "yyyy/MM/dd";
   /** @pdOid 78045ee7-736a-415b-be74-327c5204356d */
   public static String YYYYMMDDHHMINSS = "yyyy-MM-dd HH:mm:ss";

   public static String YYYYMMDDHHMMSS = "yyyyMMddhhmmss";
   public static String ISO8601="yyyy-MM-dd'T'HH:mm:ss";
   /** 将日期转换成字符串
    * 
    * @param date 
    * @param str
    * @pdOid a0a1767b-558f-4f80-8cbb-7910d328752d */
   public static String format(Date date, String str) {
      return new SimpleDateFormat(str).format(date);
   }
   
   /** 将字符串转换成日期
    * 
    * @param dateStr 
    * @param formatStr
    * @pdOid 2a77cfcd-34ec-40cc-8247-4d77720b7830 */
   public static Date parse(String dateStr, String formatStr) {
       Date d=null;
       try {
            d=new SimpleDateFormat(formatStr).parse(dateStr);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       return d;
   }

    public static String getYYYYMMDD() {
        return YYYYMMDD;
    }

    public static String getYYYYMMDDHHMINSS() {
        return YYYYMMDDHHMINSS;
    }

    /**
     * 格式化iso8601时间对象
     * @param date
     * @return
     */
    public static String formatISO8601(Date date){
        return format(date,ISO8601);
    }

    /**
     * 解析iso8601
     * @param dateStr
     * @return
     */
    public static Date parseISO8601(String dateStr){
       /* DateTimeFormatter formatter= ISODateTimeFormat.dateTime();
        return formatter.parseDateTime(dateStr).toDate();*/
        return parse(dateStr,ISO8601);
    }
    public static void main(String[] args){
        System.out.println(parse("2011-03-11T12:13:14", ISO8601));
    }
}