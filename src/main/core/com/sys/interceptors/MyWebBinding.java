package com.sys.interceptors;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * 解决日期类型无法传到后台问题
 * @author zzl
 *         Date:2014-08-05
 */
public class MyWebBinding implements WebBindingInitializer {

    public void initBinder(WebDataBinder binder, WebRequest request) {
        binder.registerCustomEditor(Date.class, new DateConvertEditor());
    }

}
