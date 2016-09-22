package com.sys.exception;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zzl
 *         Date:2014-07-28
 */
public class MyExceptionHandler implements HandlerExceptionResolver {
    private static final Logger log=Logger.getLogger(MyExceptionHandler.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exMsg=e.toString();
        log.error(exMsg);
        Map<String, Object> model = new HashMap<>();
        model.put("exceptionMessage", exMsg);
        model.put("ex", e);
        return new ModelAndView("common/error", model);
    }
}
