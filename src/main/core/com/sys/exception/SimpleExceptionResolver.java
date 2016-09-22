package com.sys.exception;

import com.sys.util.WebUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lenovo on 15-1-7.
 */
public class SimpleExceptionResolver  extends SimpleMappingExceptionResolver {
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if(WebUtil.isAjaxRequest(request)){
          return  super.doResolveException(request,response,handler,new AjaxException(ex.getMessage()));
        }else{
            return super.doResolveException(request, response, handler, ex);
        }

    }
}
