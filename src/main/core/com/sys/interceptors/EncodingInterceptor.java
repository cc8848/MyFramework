package com.sys.interceptors;

import com.sys.constant.Globals;
import com.sys.util.DateUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 编码拦截器
 * @author zzl
 * Date:2014-08-30
 */
public class EncodingInterceptor implements HandlerInterceptor {

    /**
     * 在controller后拦截
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {

    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
        request.setAttribute(Globals.ISO8601TIIME, DateUtil.formatISO8601(new Date()));
        request.setAttribute(Globals.TODAY_DATE,DateUtil.format(new Date(),DateUtil.YYYYMMDD));
        request.setAttribute(Globals.TODAY_DATE_TIME,DateUtil.format(new Date(),DateUtil.YYYYMMDDHHMINSS));
    }

    /**
     * 在controller前拦截
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml;charset=UTF-8");
        return true;
    }

}