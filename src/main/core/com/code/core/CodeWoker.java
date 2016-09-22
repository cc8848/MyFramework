/***********************************************************************
 * Module:  CodeWoker.java
 * Author:  lenovo
 * Purpose: Defines the Interface CodeWoker
 ***********************************************************************/

package com.code.core;

import java.util.*;

/**
 * 代码生成器
 */

public interface CodeWoker {
    /**
     * 生成实体
     * @return
     */
   boolean generateEntity();

    /**
     * 生成dao
     * @return
     */
   boolean generateDao();

    /**
     * 生成service
     * @return
     */
   boolean generateService();

    /**
     * 生成action
     * @return
     */
   boolean generateController();

    /**
     * 生成视图
     * @return
     */
   boolean generateView();

}