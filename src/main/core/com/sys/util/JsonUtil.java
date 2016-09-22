package com.sys.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sys.adapter.json.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zzl
 *Date:2014-07-29
 */
public class JsonUtil {
    private static Gson gson;
    static{
        gson= new GsonBuilder().registerTypeAdapter(Integer.class,new IntegerAdapter())
                .registerTypeAdapter(Long.class, new LongAdapter())
                .registerTypeAdapter(Float.class, new FloatAdapter())
                .registerTypeAdapter(Double.class,new DoubleAdapter())
                .registerTypeAdapter(Date.class,new DateAdapter())
                .setDateFormat(DateUtil.YYYYMMDD)
                .generateNonExecutableJson()
                .create();
    }
    /**
     * 转成json
     * @param o
     * @return
     */
  public static String toJson(Object o){
      return getGson().toJson(o);
  }

    /**
     * 转成对象
      * @param jsonStr
     * @param clazz
     * @return
     */
   public static Object fromJson(String jsonStr,Class clazz){
       return getGson().fromJson(jsonStr,clazz);
   }

   public static Gson getGson(){
        return gson;
   }
   public static GsonBuilder getBuilder(){
       return new GsonBuilder().registerTypeAdapter(Integer.class,new IntegerAdapter())
               .registerTypeAdapter(Float.class,new FloatAdapter())
               .registerTypeAdapter(Double.class,new DoubleAdapter())
               .registerTypeAdapter(Date.class,new DateAdapter())
               .setDateFormat(DateUtil.YYYYMMDD)
               .generateNonExecutableJson();
   }
    /**
     * response 返回json
     * @param response
     * @param o
     */
    public static void writeJson(HttpServletResponse response,Object o){
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        try {
            PrintWriter pw=response.getWriter();
            pw.write(toJson(o));
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
