/***********************************************************************
 * Module:  TemplateUtl.java
 * Author:  lenovo
 * Purpose: Defines the Class TemplateUtl
 ***********************************************************************/

package com.code.core;

import com.code.entity.TableHeadBean;

import java.io.File;
import java.util.*;

/** 模板工具类
 * 
 * @pdOid dc862aff-c526-4af9-be45-840d6fea144f */
public abstract class TemplateUtil {
   /** 由模板生成文件
    * 
    * @param templatePath 
    * @param destFilePath 
    * @param tableHeadBean
    * @pdOid 9f6db0fa-aa65-465f-86dc-bbe3c761adc6 */
   public abstract  boolean generateFileFormTemplate(String templatePath, String destFilePath, TableHeadBean tableHeadBean);

    /**
     * 由模板生成文件字符串
      * @param templatePath
     * @param tableHeadBean
     * @return
     */
   public abstract  String   generateFileFromTemplate(String templatePath,TableHeadBean tableHeadBean);



}