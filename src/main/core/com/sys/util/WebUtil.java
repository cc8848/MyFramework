package com.sys.util;

import com.sys.constant.Globals;
import com.sys.entity.SessionUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author zzl
 *         Date:2014-10-09
 */
public class WebUtil {
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
     * 判断是否是异步请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request){
        return request.getHeader("x-requested-with")!=null;
    }

    /**
     *获取session中存放的user信息
     * @return
     */
    public static SessionUser getSessionUser(){
        SessionUser user=null;
            try{
                user=(SessionUser)ContextHolderUtil.getSession().getAttribute(Globals.SESSIONUSE);
            }catch (Exception e){
               // e.printStackTrace();
                return null;
            }
        return user;
    }

    /**
     * 输出数据
     * @param response
     * @param str
     * @throws Exception
     */
    public static void writeData(HttpServletResponse response,String str) throws Exception{
        response.encodeURL("utf-8");
        PrintWriter writer=response.getWriter();
        writer.write(str);
        writer.flush();
        writer.close();

    }
}
