/***********************************************************************
 * Module:  GenerateDirector.java
 * Author:  zzl
 * Purpose: Defines the Class GenerateDirector
 ***********************************************************************/

package com.code.core;

import java.util.*;

/** 代码生成指挥者
 * 
 */
public abstract class GenerateDirector {
   public CodeWoker codeWoker;
   
   /** 生成代码
    * 
    * @param codeTypes
    * @pdOid 796c5e19-8787-48fc-a00c-6adee20a1ac6 */
   public abstract void generateCode(Set<CodeType> codeTypes);

}