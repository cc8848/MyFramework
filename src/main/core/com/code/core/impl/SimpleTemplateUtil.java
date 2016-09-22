package com.code.core.impl;

import com.code.core.TemplateUtil;
import com.code.entity.TableHeadBean;
import com.code.util.ClassUtil;
import com.code.util.ViewUtilFactory;
import com.sys.util.DateUtil;
import com.sys.util.ResourceUtil;
import com.sys.util.StringUtil;
import com.code.util.ViewUtil;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created by lenovo on 14-8-2.
 */
public class SimpleTemplateUtil extends TemplateUtil {
    private static Logger log=Logger.getLogger(SimpleTemplateUtil.class);
    @Override
    public  boolean generateFileFormTemplate(String templatePath, String destFilePath,TableHeadBean tableHeadBean) {
        BufferedWriter bw=null;
        FileOutputStream fos=null;
        try{
            fos=new FileOutputStream(destFilePath);
            bw=new BufferedWriter(new OutputStreamWriter(fos,"utf-8"));
            bw.append(generateFileFromTemplate(templatePath,tableHeadBean)).flush();
        }catch (Exception e){
            log.error(e);
            return false;
        }finally {
            if(bw!=null)
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(fos!=null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return true;
    }

    @Override
    public String generateFileFromTemplate(String templatePath, TableHeadBean tableHeadBean) {
        VelocityEngine engine=new VelocityEngine();
        Properties prop = new Properties();
        prop.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH,this.getClass().getClassLoader().getResource("/").getPath());
        // prop.setProperty(Velocity.ENCODING_DEFAULT,"utf-8");
        prop.setProperty(Velocity.INPUT_ENCODING,"utf-8");
        prop.setProperty(Velocity.OUTPUT_ENCODING,"utf-8");
        engine.init(prop);
        Template  template=engine.getTemplate(templatePath);
        VelocityContext context=new VelocityContext();
        context.put("head", tableHeadBean);
        context.put("StringUtil",new StringUtil());
        context.put("ClassUtil",new ClassUtil());
        context.put("ViewUtil", ViewUtilFactory.getInstance(tableHeadBean.getShowViewStyle()));
        context.put("DateUtil",new DateUtil());
        context.put("today",DateUtil.format(new Date(),DateUtil.YYYYMMDD));
        StringWriter writer=new StringWriter();
        template.merge(context,writer);
        return writer.toString();
    }
}
