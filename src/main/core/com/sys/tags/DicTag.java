package com.sys.tags;

import com.code.entity.TableFieldBean;
import com.sys.util.DicUtil;
import com.sys.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Created by admin on 2014/11/14.
 */
public class DicTag extends BodyTagSupport {
    private String dicKey;
    private String dicVal;
    private String dicTable;
    private String code;
    private String dicDefault;
    @Override
    public int doEndTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        TableFieldBean fieldBean=new TableFieldBean();
        fieldBean.setDicKey(dicKey);
        fieldBean.setDicTable(dicTable);
        fieldBean.setDicValue(dicVal);
        fieldBean.setFieldDefault(dicDefault);
        Object o= DicUtil.getDicValue(fieldBean,code,false);
        try {
            if(o!=null)
                out.print(o + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doEndTag();
    }

    public String getDicKey() {
        return dicKey;
    }

    public void setDicKey(String dicKey) {
        this.dicKey = dicKey;
    }

    public String getDicVal() {
        return dicVal;
    }

    public void setDicVal(String dicVal) {
        this.dicVal = dicVal;
    }

    public String getDicTable() {
        return dicTable;
    }

    public void setDicTable(String dicTable) {
        this.dicTable = dicTable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDicDefault() {
        return dicDefault;
    }

    public void setDicDefault(String dicDefault) {
        this.dicDefault = dicDefault;
    }
}
