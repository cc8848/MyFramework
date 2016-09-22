package com.code.tags;

import com.code.entity.ModuleExtEntity;
import com.code.util.ModulesUtil;
import org.apache.commons.collections.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.List;

public class ModulesTag extends BodyTagSupport {
    private String tableName;

    @Override
    public int doEndTag() throws JspException {
        HttpServletRequest request= (HttpServletRequest) this.pageContext.getRequest();
        if(tableName.contains("businesscore/business_aj.do?toList&projectId=")){
            tableName = "businesscore/business_aj.do?toList";
        }
        if(tableName.contains("businesscore/business_wj.do?toList&pid=")){
            tableName = "businesscore/business_wj.do?toList";
        }
        request.setAttribute("_modulesList", ModulesUtil.getModulesLink(tableName));
        /*JspWriter out = this.pageContext.getOut();
        try {
            out.print(ModulesUtil.getModulesLink(tableName, moduleType));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return super.doEndTag();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
