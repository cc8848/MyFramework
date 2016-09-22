package com.sys.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zzl
 *         Date:2014-08-04
 */
public class ResourceUtil {
    /**
     * 获取资源路径
     * @param name
     * @return
     */
    public static String getResourcePath(String name){
        URL url= ResourceUtil.class.getClassLoader().getResource(name);
        return url.getPath();
    }

    /**
     * 获取资源
     * @param name
     * @return
     */
    public static Map<String,String> getResource(String name){
        Map<String,String> map=new Hashtable<>();
        Properties p = new Properties();
        URL url= ResourceUtil.class.getClassLoader().getResource(name);
        BufferedInputStream in=null;
        try {
            in=(BufferedInputStream)url.getContent();
            p.load(in);
            for(Object o:p.keySet()){
                map.put((String)o,p.getProperty((String)o));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
    /**
     * 获得请求路径
     *
     * @param request
     * @return
     */
    public static String getRequestPath(HttpServletRequest request,boolean containMethod) {
        String requestPath = request.getRequestURI();
        if(containMethod&& StringUtils.isNotBlank(request.getQueryString()))
            requestPath+="?" + request.getQueryString();
        /*if (requestPath.indexOf("&") > -1) {// 去掉其他参数
            requestPath = requestPath.substring(0, requestPath.indexOf("&"));
        }*/
        requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
        return requestPath;
    }

    /**
     * 获取ip
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "本地";
        }
        return ip;
    }

    /**
     * 将request中值不为空的参数转成map
     * @param request
     * @return
     */
    public static Map<String,Object> getParamMap(HttpServletRequest request){
        Map<String,Object> paramMap=new ConcurrentHashMap<>();
        for(Object str:request.getParameterMap().keySet()){
             Object val=request.getParameterMap().get(str);
             if(val!=null&&val.getClass().isArray()){
                 String[] vals= (String[]) val;
                 if(vals!=null&&vals.length>0){
                     if(StringUtils.isNotBlank(vals[0]))
                        paramMap.put(str.toString(),vals[0]);
                 }
             }
        }
        return paramMap;
    }
    /**
     * 将request中值不为空的参数转成map
     * map value为数组
     * @param request
     * @return
     */
    public static Map<String,Object[]> getParamsMap(HttpServletRequest request){
        Map<String,Object[]> paramMap=new ConcurrentHashMap<>();
        for(Object str:request.getParameterMap().keySet()){
            Object val=request.getParameterMap().get(str);
            if(val!=null&&val.getClass().isArray()){
                String[] vals= (String[]) val;
                if(vals!=null&&vals.length>0){
                    if(StringUtils.isNotBlank(vals[0]))
                        paramMap.put(str.toString(),vals);
                }
            }
        }
        return paramMap;
    }
}
