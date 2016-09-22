package com.sys.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zzl
 *         Date:2014-08-14
 */
public class ContextHolderUtil {
    public static HttpServletRequest getRequest() {
        RequestAttributes ra=null;
        try{
            ra=RequestContextHolder.currentRequestAttributes();
        }catch (Exception e){

        }
        if(ra==null)
            return null;
        HttpServletRequest request = ((ServletRequestAttributes)ra ).getRequest();
        return request;

    }
    /**
     * SpringMvc下获取session
     *
     * @return
     */
    public static HttpSession getSession() {
        HttpServletRequest request=getRequest();
        if(request==null)
            return null;
        HttpSession session = getRequest().getSession();
        return session;

    }

    /**
     * 获取请求的url
     * @return
     */
    public static String getRequestUrl(){
        HttpServletRequest request=getRequest();
        String contextPath=request.getContextPath();
        String url=request.getRequestURI();
        String queryStr=request.getQueryString();
        StringBuffer urlBuffer=new StringBuffer();
        if(StringUtils.isNotBlank(url)&&contextPath!=null&&url.indexOf(contextPath)!=-1){
            urlBuffer.append(url.substring(url.indexOf(contextPath)+contextPath.length()+1));
        }
        if(StringUtils.isNotBlank(queryStr)){
            urlBuffer.append("?").append(queryStr);
        }
        return urlBuffer.toString();
    }
}
