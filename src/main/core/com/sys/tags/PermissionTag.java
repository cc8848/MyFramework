package com.sys.tags;

import com.sys.util.AuthUtil;
import com.sys.util.ResourceUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * 权限控制标签
 * Created by lenovo on 2014/8/27.
 */
public class PermissionTag extends BodyTagSupport {
    private String buttonCode;
    private String actionUrl;
    private boolean show=true;
    @Override
    public int doStartTag() throws JspException {
        String url=actionUrl;
        HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
        if(StringUtils.isBlank(url)&&show){
            return EVAL_BODY_INCLUDE;
            //url=ResourceUtil.getRequestPath(request,false);
        }else{
            if(url.indexOf(request.getContextPath())!=-1){
                url=url.substring(url.indexOf(request.getContextPath())+request.getContextPath().length()+1);
            }
        }

        if(!AuthUtil.needAuth(url,buttonCode)&&show|| AuthUtil.hasButtonAuth(url,buttonCode)){
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {

        return super.doEndTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }

    public String getButtonCode() {
        return buttonCode;
    }

    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
